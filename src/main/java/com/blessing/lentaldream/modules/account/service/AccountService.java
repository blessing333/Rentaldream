package com.blessing.lentaldream.modules.account.service;

import com.blessing.lentaldream.modules.account.UserAccount;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.account.domain.AccountTag;
import com.blessing.lentaldream.modules.account.form.SignUpForm;
import com.blessing.lentaldream.modules.account.repository.AccountRepository;
import com.blessing.lentaldream.modules.account.repository.AccountTagRepository;
import com.blessing.lentaldream.modules.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor

public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountTagRepository accountTagRepository;

    public Account processSignUp(SignUpForm signUpForm){
        String encodedPassword = passwordEncoder.encode(signUpForm.getPassword());
        Account newAccount = Account.createNewAccount(signUpForm.getNickname(), signUpForm.getEmail(),encodedPassword);
        accountRepository.save(newAccount);
        return newAccount;
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(userEmail);
        if (account == null) {
            throw new UsernameNotFoundException(userEmail);
        }
        return new UserAccount(account);
    }

    public void addTag(Long accountId, Tag tag){
        Optional<Account> findAccount = accountRepository.findById(accountId);
        findAccount.ifPresent(account ->{
            AccountTag accountTag = AccountTag.createNewAccountTag(account,tag);
            account.addNewAccountTag(accountTag);
        });
    }

    public void deleteTag(Long accountId, Tag tag) {
        Optional<Account> findAccount = accountRepository.findById(accountId);
        findAccount.ifPresent(account ->{
            AccountTag accountTag = accountTagRepository.findByAccountAndTag(account,tag);
            account.deleteAccountTag(accountTag);
        });
    }

    public List<String> getTagNameList(Long accountId) {
        Optional<Account> findAccount = accountRepository.findById(accountId);
        List<String> list = new ArrayList<>();
        findAccount.ifPresent(account ->{
            account.getAccountTags().forEach(accountTag -> {
                list.add(accountTag.getTag().getTagName());
            });
        });
        return list;
    }
}
