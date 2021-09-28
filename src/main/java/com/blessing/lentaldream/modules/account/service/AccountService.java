package com.blessing.lentaldream.modules.account.service;

import com.blessing.lentaldream.modules.account.UserAccount;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.account.domain.AccountTag;
import com.blessing.lentaldream.modules.account.domain.AccountZone;
import com.blessing.lentaldream.modules.account.favorite.Favorite;
import com.blessing.lentaldream.modules.account.form.PasswordForm;
import com.blessing.lentaldream.modules.account.form.ProfileForm;
import com.blessing.lentaldream.modules.account.form.SignUpForm;
import com.blessing.lentaldream.modules.account.repository.AccountRepository;
import com.blessing.lentaldream.modules.account.repository.AccountTagRepository;
import com.blessing.lentaldream.modules.account.repository.AccountZoneRepository;
import com.blessing.lentaldream.modules.post.PostService;
import com.blessing.lentaldream.modules.post.domain.Post;
import com.blessing.lentaldream.modules.tag.Tag;
import com.blessing.lentaldream.modules.zone.Zone;
import com.blessing.lentaldream.modules.zone.ZoneForm;
import com.blessing.lentaldream.modules.zone.ZoneService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ZoneService zoneService;
    private final AccountTagRepository accountTagRepository;
    private final AccountZoneRepository accountZoneRepository;
    private final PostService postService;
    private final ModelMapper modelMapper;

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

    public List<String> getZoneList(Long accountId) {
        Optional<Account> findAccount = accountRepository.findById(accountId);
        List<String> list = new ArrayList<>();
        findAccount.ifPresent(account ->{
            account.getAccountZones().forEach(accountZone -> {
                list.add(accountZone.getZone().toString());
            });
        });
        return list;
    }

    public void addZone(Long accountId,ZoneForm zoneForm) {
        Optional<Account> foundAccount = accountRepository.findById(accountId);
        Zone zone = zoneService.findByCityAndProvince(zoneForm.getCity(),zoneForm.getProvince());
        foundAccount.ifPresent(account -> {
            AccountZone newAccountZone = AccountZone.createNewAccountZone(account, zone);
            account.addNewAccountZone(newAccountZone);
        });
    }

    public void removeZone(Long id, Zone zone) {
        Optional<Account> foundAccount = accountRepository.findById(id);
        foundAccount.ifPresent(account -> {
            AccountZone accountZone = accountZoneRepository.findByAccountAndZone(account,zone);
            account.deleteAccountZone(accountZone);
        });
    }

    public void updateProfile(Long accountId, ProfileForm profileForm) {
        Optional<Account> foundAccount = accountRepository.findById(accountId);
        foundAccount.ifPresent(account -> {
            account.updateProfile(profileForm);
        });
    }

    public void changeAccountPassword(Long accountId, PasswordForm passwordForm) {
        Optional<Account> foundAccount = accountRepository.findById(accountId);
        foundAccount.ifPresent(account -> {
            String encodedPassword = passwordEncoder.encode(passwordForm.getNewPassword());
            account.changePassword(encodedPassword);
        });
    }

    public void addFavoriteToAccount(Account account, Long postId){
        Account foundAccount = accountRepository.findByNickname(account.getNickname());
        Post post = postService.loadPostInformation(postId);
        Favorite favorite = Favorite.createNewFavorite(account, post);
        if(!foundAccount.checkFavorite(post)) {
            post.increaseFavoriteCount();
            foundAccount.addFavorite(favorite);
        }
    }

    public void deleteFavoriteFromAccount(Account account, Long postId) {
        Account foundAccount = accountRepository.findByNickname(account.getNickname());
        Post post = postService.loadPostInformation(postId);
        Favorite target = Favorite.createNewFavorite(account,post);
        if(foundAccount.checkFavorite(post)) {
            foundAccount.deleteFavorite(target);
            post.decreaseFavoriteCount();
        }
    }
}
