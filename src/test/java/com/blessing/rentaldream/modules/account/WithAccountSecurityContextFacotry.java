package com.blessing.rentaldream.modules.account;

import com.blessing.rentaldream.modules.account.form.SignUpForm;
import com.blessing.rentaldream.modules.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;



@RequiredArgsConstructor
public class WithAccountSecurityContextFacotry implements WithSecurityContextFactory<LoginWith> {

    private final AccountService accountService;

    @Override
    public SecurityContext createSecurityContext(LoginWith loginWith) {
        String nickname = loginWith.value();
        String email = nickname + "@email.com";
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname(nickname);
        signUpForm.setEmail(email);
        signUpForm.setPassword("12345678");
        accountService.processSignUp(signUpForm);

        UserDetails principal = accountService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
