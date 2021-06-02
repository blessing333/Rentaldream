package com.blessing.lentaldream.modules.account.service;

import com.blessing.lentaldream.modules.account.UserAccount;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.account.form.SignUpForm;
import com.blessing.lentaldream.modules.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account processSignUp(SignUpForm signUpForm){
        String encodedPassword = passwordEncoder.encode(signUpForm.getPassword());
        Account newAccount = Account.createNewAccount(signUpForm.getNickname(), signUpForm.getEmail(),encodedPassword);
        accountRepository.save(newAccount);
        //sendConfirmEmail();
        return newAccount;
    }

//    private void sendConfirmEmail(Account newAccount) {
//        Context context = new Context();
//        context.setVariable("link", "/check-email-token?token=" + newAccount.getEmailCheckToken() +
//                "&email=" + newAccount.getEmail());
//        context.setVariable("nickname", newAccount.getNickname());
//        context.setVariable("linkName", "이메일 인증하기");
//        context.setVariable("message", "스터디올래 서비스를 사용하려면 링크를 클릭하세요.");
//        context.setVariable("host", appProperties.getHost());
//        String message = templateEngine.process("mail/simple-link", context);
//
//        EmailMessage emailMessage = EmailMessage.builder()
//                .to(newAccount.getEmail())
//                .subject("스터디올래, 회원 가입 인증")
//                .message(message)
//                .build();
//
//        emailService.sendEmail(emailMessage);
//    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
