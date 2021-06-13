package com.blessing.lentaldream.modules.account.controller;

import com.blessing.lentaldream.modules.account.AccountFactory;
import com.blessing.lentaldream.modules.account.LoginWith;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.account.domain.AccountTag;
import com.blessing.lentaldream.modules.account.repository.AccountRepository;
import com.blessing.lentaldream.modules.account.repository.AccountTagRepository;
import com.blessing.lentaldream.modules.tag.Tag;
import com.blessing.lentaldream.modules.tag.TagForm;
import com.blessing.lentaldream.modules.tag.TagRepository;
import com.blessing.lentaldream.modules.tag.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.blessing.lentaldream.infra.config.UrlConfig.ACCOUNT_SETTING_PROFILE_URL;
import static com.blessing.lentaldream.infra.config.UrlConfig.ACCOUNT_SETTING_TAG_URL;
import static com.blessing.lentaldream.infra.config.ViewNameConfig.ACCOUNT_SETTING_PROFILE_VIEW;
import static com.blessing.lentaldream.infra.config.ViewNameConfig.ACCOUNT_SETTING_TAG_VIEW;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SettingControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired AccountFactory accountFactory;
    @Autowired AccountTagRepository accountTagRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired TagService tagService;
    @Autowired ObjectMapper objectMapper;
    @Autowired TagRepository tagRepository;
    private static final String TEST_USER = "testUser";

    @DisplayName("유저 프로필 화면 조회")
    @LoginWith(TEST_USER)
    @Test
    public void accountProfileViewTest() throws Exception {
        mockMvc.perform(get(ACCOUNT_SETTING_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(ACCOUNT_SETTING_PROFILE_VIEW));
    }

    @DisplayName("유저 관심 태그 화면 조회")
    @LoginWith(TEST_USER)
    @Test
    public void accountTagViewTest() throws Exception {
        mockMvc.perform(get(ACCOUNT_SETTING_TAG_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(ACCOUNT_SETTING_TAG_VIEW))
                .andExpect(model().attributeExists("tags","account","whitelist"));
    }

    @DisplayName("유저 관심 태그 추가")
    @LoginWith(TEST_USER)
    @Test
    public void accountTagAddTest() throws Exception {
        String tagName = "testTag";
        TagForm tagForm = new TagForm(tagName);

        mockMvc.perform(post(ACCOUNT_SETTING_TAG_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(tagForm))
        .with(csrf()))
                .andExpect(status().isOk());
        Tag foundTag = tagRepository.findByTagName(tagName);
        Assertions.assertNotNull(foundTag);
        Account foundAccount = accountRepository.findByNickname(TEST_USER);
        AccountTag result = accountTagRepository.findByAccountAndTag(foundAccount, foundTag);
        Assertions.assertNotNull(result);
    }

    @DisplayName("유저 관심 태그 삭제")
    @LoginWith(TEST_USER)
    @Test
    public void accountTagRemoveTest() throws Exception {
        String tagName = "testTag";
        Account account = accountRepository.findByNickname(TEST_USER);
        Tag tag = tagService.addNewTag(tagName);
        AccountTag newAccountTag = AccountTag.createNewAccountTag(account, tag);
        account.addNewAccountTag(newAccountTag);

        TagForm tagForm = new TagForm(tagName);

        mockMvc.perform(delete(ACCOUNT_SETTING_TAG_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm))
                .with(csrf()))
                .andExpect(status().isOk());
        Tag foundTag = tagRepository.findByTagName(tagName);
        Assertions.assertNotNull(foundTag);

        Account foundAccount = accountRepository.findByNickname(TEST_USER);
        AccountTag result = accountTagRepository.findByAccountAndTag(foundAccount, foundTag);
        Assertions.assertNull(result);
    }
}