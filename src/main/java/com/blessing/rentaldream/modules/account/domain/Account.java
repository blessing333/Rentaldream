package com.blessing.rentaldream.modules.account.domain;

import com.blessing.rentaldream.modules.account.form.ProfileForm;
import com.blessing.rentaldream.modules.post.domain.Post;
import com.blessing.rentaldream.modules.tag.Tag;
import com.blessing.rentaldream.modules.zone.Zone;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true,nullable = false)
    private String nickname;

    @OneToMany(mappedBy ="account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccountTag> AccountTags = new HashSet<>();

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccountZone> AccountZones= new HashSet<>();

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WishList> wishLists = new HashSet<>();

    private String password;

    private LocalDateTime joinedAt;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private boolean receivePostCreatedNotificationByEmail;

    private boolean receivePostCreatedNotificationByWeb = true;

    public static Account createNewAccount(String nickname, String email, String password) {
        Account instance = new Account();
        instance.setNickname(nickname);
        instance.setEmail(email);
        instance.setPassword(password);
        instance.setJoinedAt(LocalDateTime.now());
        return instance;
    }

    public void addNewAccountTag(AccountTag accountTag) {
        this.getAccountTags().add(accountTag);
    }

    public void deleteAccountTag(AccountTag accountTag){
        this.getAccountTags().remove(accountTag);
    }

    public void addNewAccountZone(AccountZone newAccountZone) {
        this.getAccountZones().add(newAccountZone);
    }

    public void deleteAccountZone(AccountZone accountZone) {
        this.getAccountZones().remove(accountZone);
    }

    public void updateProfile(ProfileForm profileForm) {
        setNickname(profileForm.getNickname());
        setBio(profileForm.getBio());
        setUrl(profileForm.getUrl());
        setLocation(profileForm.getLocation());
        setReceivePostCreatedNotificationByEmail(profileForm.isReceivePostCreatedNotificationByEmail());
    }
    public void addFavorite(WishList wishList){
        this.getWishLists().add(wishList);
    }

    public void deleteFavorite(WishList wishList){
        this.getWishLists().remove(wishList);
    }

    public boolean checkFavorite(Post post){
        Set<WishList> wishListSet = this.getWishLists();
        boolean result = false;
        for(WishList wishList : wishListSet){
            if(Objects.equals(wishList.getPost().getId(), post.getId())) {
                return true;
            }
        }
        return false;
    }
    public void changePassword(String newPassword) {
        setPassword(newPassword);
    }

    public List<Tag> convertAccountTagsAsTagList(){
        return getAccountTags().stream().map(AccountTag::getTag).collect(Collectors.toList());
    }

    public List<Zone> convertAccountZonesAsZoneList(){
        return getAccountZones().stream().map(AccountZone::getZone).collect(Collectors.toList());
    }
}
