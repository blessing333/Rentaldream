package com.blessing.lentaldream.modules.account.controller;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.account.form.SignUpForm;
import com.blessing.lentaldream.modules.account.service.AccountService;
import com.blessing.lentaldream.modules.account.validator.SignUpFormValidator;
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

import static com.blessing.lentaldream.infra.config.UrlConfig.*;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private static final String SIGN_UP_VIEW = "account/sign-up";
    private static final String LOGIN_VIEW = "account/login";

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

//    @GetMapping(LOGIN_URL)
//    public String createLoginView(){
//        return LOGIN_VIEW;
//    }
}
