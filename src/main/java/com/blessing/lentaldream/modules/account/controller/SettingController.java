package com.blessing.lentaldream.modules.account.controller;

import com.blessing.lentaldream.modules.account.CurrentUser;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.account.form.ProfileForm;
import com.blessing.lentaldream.modules.account.repository.AccountRepository;
import com.blessing.lentaldream.modules.account.service.AccountService;
import com.blessing.lentaldream.modules.account.validator.ProfileFormValidator;
import com.blessing.lentaldream.modules.tag.Tag;
import com.blessing.lentaldream.modules.tag.TagForm;
import com.blessing.lentaldream.modules.tag.TagService;
import com.blessing.lentaldream.modules.zone.Zone;
import com.blessing.lentaldream.modules.zone.ZoneForm;
import com.blessing.lentaldream.modules.zone.ZoneService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.blessing.lentaldream.infra.config.UrlConfig.*;
import static com.blessing.lentaldream.infra.config.ViewNameConfig.*;

@Controller
@RequiredArgsConstructor
public class SettingController {
    private final ProfileFormValidator profileFormValidator;
    private final ObjectMapper objectMapper;
    private final TagService tagService;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final ZoneService zoneService;
    private final ModelMapper modelMapper;

    @InitBinder("profileForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(profileFormValidator);
    }

    @GetMapping(ACCOUNT_SETTING_PROFILE_URL)
    public String createAccountProfileView(@CurrentUser Account account,Model model){
        Account foundAccount = accountRepository.findById(account.getId()).get();
        ProfileForm profileForm = modelMapper.map(foundAccount, ProfileForm.class);
        model.addAttribute("account",foundAccount);
        model.addAttribute(profileForm);
        return ACCOUNT_SETTING_PROFILE_VIEW;
    }

    @PostMapping(ACCOUNT_SETTING_PROFILE_URL)
    public String editAccountProfile(@CurrentUser Account account, @Valid @ModelAttribute ProfileForm profileForm, Errors errors, Model model){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return ACCOUNT_SETTING_PROFILE_VIEW;
        }
        accountService.updateProfile(account.getId(),profileForm);
        return REDIRECT_URL + ACCOUNT_SETTING_PROFILE_URL;
    }

    @GetMapping(ACCOUNT_SETTING_TAG_URL)
    public String createAccountTagSettingView(@CurrentUser Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        List<String> tagNameList = accountService.getTagNameList(account.getId());
        model.addAttribute("tags", tagNameList);
        List<String> allTags = tagService.findAllTag().stream().map(Tag::getTagName).collect(Collectors.toList());
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
        Tag tag = tagService.findByTagName(tagName);
        if(tag == null){
            return ResponseEntity.badRequest().build();
        }
        accountService.deleteTag(account.getId(),tag);
        return ResponseEntity.ok().build();
    }

    @GetMapping(ACCOUNT_SETTING_ZONE_URL)
    public String createAccountZoneView(@CurrentUser Account account,Model model) throws JsonProcessingException {
        model.addAttribute(account);
        List<String> zoneList = accountService.getZoneList(account.getId());
        model.addAttribute("zones", zoneList);
        List<String> allZone = zoneService.findAllZones().stream().map(Zone::toString).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allZone));
        return ACCOUNT_SETTING_ZONE_VIEW;
    }

    @PostMapping(ACCOUNT_SETTING_ZONE_URL)
    public ResponseEntity addAccountZone(@CurrentUser Account account, @RequestBody ZoneForm zoneForm){
        accountService.addZone(account.getId(), zoneForm);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(ACCOUNT_SETTING_ZONE_URL)
    public ResponseEntity removeAccountZone(@CurrentUser Account account, @RequestBody ZoneForm zoneForm){
        Zone zone = zoneService.findByCityAndProvince(zoneForm.getCity(),zoneForm.getProvince());
        if(zone == null){
            return ResponseEntity.badRequest().build();
        }
        accountService.removeZone(account.getId(), zone);
        return ResponseEntity.ok().build();
    }
}
