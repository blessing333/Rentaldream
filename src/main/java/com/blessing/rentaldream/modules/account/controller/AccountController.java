package com.blessing.rentaldream.modules.account.controller;

import com.blessing.rentaldream.modules.account.domain.Account;
import com.blessing.rentaldream.modules.account.form.SignUpForm;
import com.blessing.rentaldream.modules.account.service.AccountService;
import com.blessing.rentaldream.modules.account.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

import static com.blessing.rentaldream.infra.config.UrlConfig.*;
import static com.blessing.rentaldream.infra.config.ViewNameConfig.SIGN_UP_VIEW;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping(SIGN_UP_URL)
    public String createSignUpView(Model model){
        SignUpForm signUpForm = new SignUpForm();
        model.addAttribute(signUpForm);
        return SIGN_UP_VIEW;
    }

    @PostMapping(SIGN_UP_URL)
    public String processSignUp(@ModelAttribute @Valid SignUpForm form, Errors error,Model model){
        if(error.hasErrors()){
            return SIGN_UP_VIEW;
        }
        Account account = accountService.processSignUp(form);
        accountService.login(account);
        return REDIRECT_URL+HOME_URL;
    }

}
