package com.blessing.lentaldream.modules.account.controller;

import com.blessing.lentaldream.modules.account.CurrentUser;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.account.service.AccountService;
import com.blessing.lentaldream.modules.tag.Tag;
import com.blessing.lentaldream.modules.tag.TagForm;
import com.blessing.lentaldream.modules.tag.TagRepository;
import com.blessing.lentaldream.modules.tag.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

import static com.blessing.lentaldream.infra.config.UrlConfig.ACCOUNT_SETTING_PROFILE_URL;
import static com.blessing.lentaldream.infra.config.UrlConfig.ACCOUNT_SETTING_TAG_URL;
import static com.blessing.lentaldream.infra.config.ViewNameConfig.ACCOUNT_SETTING_PROFILE_VIEW;
import static com.blessing.lentaldream.infra.config.ViewNameConfig.ACCOUNT_SETTING_TAG_VIEW;

@Controller
@RequiredArgsConstructor
public class SettingController {
    private final ObjectMapper objectMapper;
    private final TagService tagService;
    private final TagRepository tagRepository;
    private final AccountService accountService;

    @GetMapping(ACCOUNT_SETTING_PROFILE_URL)
    public String createAccountProfileView(@CurrentUser Account account){
        return ACCOUNT_SETTING_PROFILE_VIEW;
    }

    @GetMapping(ACCOUNT_SETTING_TAG_URL)
    public String createAccountTagSettingView(@CurrentUser Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        List<String> tagNameList = accountService.getTagNameList(account.getId());
        model.addAttribute("tags", tagNameList);
        List<String> allTags = tagRepository.findAll().stream().map(Tag::getTagName).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allTags));
        return ACCOUNT_SETTING_TAG_VIEW;
    }

    @PostMapping(ACCOUNT_SETTING_TAG_URL)
    public ResponseEntity addTag(@CurrentUser Account account, @RequestBody TagForm tagForm){
        Tag tag = tagService.addNewTag(tagForm.getTagName());
        accountService.addTag(account.getId(),tag);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping(ACCOUNT_SETTING_TAG_URL)
    public ResponseEntity deleteTag(@CurrentUser Account account, @RequestBody TagForm tagForm){
        String tagName = tagForm.getTagName();
        Tag tag = tagRepository.findByTagName(tagName);
        if(tag == null){
            return ResponseEntity.badRequest().build();
        }
        accountService.deleteTag(account.getId(),tag);
        return ResponseEntity.ok().build();
    }
}
