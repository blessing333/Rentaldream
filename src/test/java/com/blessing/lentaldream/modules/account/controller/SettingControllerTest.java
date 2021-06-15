package com.blessing.lentaldream.modules.account.controller;

import com.blessing.lentaldream.modules.account.AccountFactory;
import com.blessing.lentaldream.modules.account.LoginWith;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.account.domain.AccountTag;
import com.blessing.lentaldream.modules.account.domain.AccountZone;
import com.blessing.lentaldream.modules.account.form.ProfileForm;
import com.blessing.lentaldream.modules.account.repository.AccountRepository;
import com.blessing.lentaldream.modules.account.repository.AccountTagRepository;
import com.blessing.lentaldream.modules.account.repository.AccountZoneRepository;
import com.blessing.lentaldream.modules.tag.Tag;
import com.blessing.lentaldream.modules.tag.TagForm;
import com.blessing.lentaldream.modules.tag.TagRepository;
import com.blessing.lentaldream.modules.tag.TagService;
import com.blessing.lentaldream.modules.zone.Zone;
import com.blessing.lentaldream.modules.zone.ZoneForm;
import com.blessing.lentaldream.modules.zone.ZoneRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.blessing.lentaldream.infra.config.UrlConfig.*;
import static com.blessing.lentaldream.infra.config.ViewNameConfig.*;
import static org.junit.jupiter.api.Assertions.*;
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
    @Autowired ZoneRepository zoneRepository;
    @Autowired AccountZoneRepository accountZoneRepository;

    private static final String TEST_USER = "testUser";
    private static final String PROFILE_FORM_NAME = "profileForm";
    private Zone testZone = Zone.builder().city("test").localCityName("테스트시").province("테스트주").build();

    @DisplayName("유저 프로필 화면 조회")
    @LoginWith(TEST_USER)
    @Test
    public void accountProfileViewTest() throws Exception {
        mockMvc.perform(get(ACCOUNT_SETTING_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(PROFILE_FORM_NAME,"account"))
                .andExpect(view().name(ACCOUNT_SETTING_PROFILE_VIEW));
    }

    @DisplayName("유저 프로필 갱신 - 입력값 정상")
    @LoginWith(TEST_USER)
    @Test
    public void accountProfileUpdateTest() throws Exception {
        Account account = accountRepository.findByNickname(TEST_USER);
        Long accountId = account.getId();

        ProfileForm profileForm = new ProfileForm();
        profileForm.setCurrentNickname(TEST_USER);
        profileForm.setNickname("changedNickname");
        profileForm.setBio("changed bio");
        profileForm.setUrl("http://changedpage.com");
        profileForm.setLocation("changedLocation");

        mockMvc.perform(post(ACCOUNT_SETTING_PROFILE_URL)
            .param("nickname",profileForm.getNickname())
            .param("currentNickname",profileForm.getCurrentNickname())
            .param("bio", profileForm.getBio())
            .param("url",profileForm.getUrl())
            .param("location",profileForm.getLocation())
            .with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ACCOUNT_SETTING_PROFILE_URL));
        Account changedAccount =accountRepository.findByNickname(profileForm.getNickname());
        assertNotNull(changedAccount);
        assertEquals(changedAccount.getBio(),profileForm.getBio());
        assertEquals(changedAccount.getUrl(),profileForm.getUrl());
        assertEquals(changedAccount.getNickname(),profileForm.getNickname());
        assertEquals(changedAccount.getLocation(),profileForm.getLocation());

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
        assertNotNull(foundTag);
        Account foundAccount = accountRepository.findByNickname(TEST_USER);
        AccountTag result = accountTagRepository.findByAccountAndTag(foundAccount, foundTag);
        assertNotNull(result);
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
        assertNotNull(foundTag);

        Account foundAccount = accountRepository.findByNickname(TEST_USER);
        AccountTag result = accountTagRepository.findByAccountAndTag(foundAccount, foundTag);
        assertNull(result);
    }

    @DisplayName("유저 활동 지역 화면 조회")
    @LoginWith(TEST_USER)
    @Test
    public void accountZooneViewTest() throws Exception {
        mockMvc.perform(get(ACCOUNT_SETTING_ZONE_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(ACCOUNT_SETTING_ZONE_VIEW))
                .andExpect(model().attributeExists("zones","account","whitelist"));
    }

    @DisplayName("유저 활동 지역 태그 추가")
    @LoginWith(TEST_USER)
    @Test
    public void accountZoneAddTest() throws Exception {
        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());
        zoneRepository.save(testZone);

        mockMvc.perform(post(ACCOUNT_SETTING_ZONE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zoneForm))
                .with(csrf()))
                .andExpect(status().isOk());

        Account foundAccount = accountRepository.findByNickname(TEST_USER);
        AccountZone result = accountZoneRepository.findByAccountAndZone(foundAccount, testZone);
        assertNotNull(result);
    }

    @DisplayName("유저 관심 태그 삭제")
    @LoginWith(TEST_USER)
    @Test
    public void accountZoneRemoveTest() throws Exception {
        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());
        zoneRepository.save(testZone);
        Account account = accountRepository.findByNickname(TEST_USER);

        AccountZone newAccountZone = AccountZone.createNewAccountZone(account, testZone);
        account.addNewAccountZone(newAccountZone);

        mockMvc.perform(delete(ACCOUNT_SETTING_ZONE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zoneForm))
                .with(csrf()))
                .andExpect(status().isOk());

        Account foundAccount = accountRepository.findByNickname(TEST_USER);
        AccountZone result = accountZoneRepository.findByAccountAndZone(foundAccount, testZone);
        assertNull(result);
    }
}