package com.blessing.lentaldream.modules.account.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String nickname;

    @OneToMany(mappedBy ="account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccountTag> AccountTags = new HashSet<>();

//    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
//    private Set<AccountZone> AccountZones= new HashSet<>();

    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime joinedAt;

    private LocalDateTime tokenGeneratedAt;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb = true;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb = true;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb = true;

    public static Account createNewAccount(String nickname, String email, String password) {
        Account instance = new Account();
        instance.setNickname(nickname);
        instance.setEmail(email);
        instance.setPassword(password);
        instance.setJoinedAt(LocalDateTime.now());
        instance.generateEmailCheckToken();
        return instance;
    }

    public void generateEmailCheckToken() {
        this.emailCheckToken= UUID.randomUUID().toString();
        this.tokenGeneratedAt = LocalDateTime.now();
    }
    public boolean isValidToken(String token) {
        return this.getEmailCheckToken().equals(token);
    }
    public boolean canResendEmail(){
        return tokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
    }

    public void addNewAccountTag(AccountTag accountTag) {
        this.getAccountTags().add(accountTag);
    }

    public void deleteAccountTag(AccountTag accountTag){
        this.getAccountTags().remove(accountTag);
    }
}
