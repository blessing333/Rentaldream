package com.blessing.lentaldream.modules.post.domain;

import com.blessing.lentaldream.modules.account.UserAccount;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.comment.Comment;
import com.blessing.lentaldream.modules.post.form.PostForm;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Post {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account createdBy;

    private String title;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String thumbnail;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String description;

    @OneToMany(mappedBy ="post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> tags = new HashSet<>();

    @OneToMany(mappedBy ="post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostZone> zones = new HashSet<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    private int price;
    private LocalDateTime createdDate;
    private int viewCount = 0;
    private int favoriteCount = 0;

    public static Post createNewPost(String title, Account account,String description, String thumbnail, int price) {
        Post instance = new Post();
        instance.setTitle(title);
        instance.setCreatedBy(account);
        instance.setDescription(description);
        instance.setThumbnail(thumbnail);
        instance.setCreatedDate(LocalDateTime.now());
        instance.setPrice(price);
        return instance;
    }

    public void increaseViewCount(){
        this.viewCount++;
    }
    public void increaseFavoriteCount(){ this.favoriteCount++;}
    public void decreaseFavoriteCount(){this.favoriteCount--;}
    public void addTag(PostTag tag){
        this.getTags().add(tag);
    }
    public void addZone(PostZone postZone) {
        this.getZones().add(postZone);
    }
    public boolean isPoster(UserAccount userAccount){
        Account account = userAccount.getAccount();
        return createdBy.equals(account);
    }
    public boolean isPoster(Account account){
        return createdBy.equals(account);
    }
    public List<String> getAllTagName(){
        return this.getTags().stream().map((PostTag tag)-> tag.getTag().getTagName()).collect(Collectors.toList());
    }
    public List<String> getAllZoneName(){
        return this.getZones().stream().map((PostZone zone)-> zone.getZone().toString()).collect(Collectors.toList());
    }

    public void modifyPost(PostForm postForm) {
        this.setTitle(postForm.getTitle());
        this.setDescription(postForm.getDescription());
        this.setThumbnail(postForm.getThumbnail());
        this.setPrice(postForm.getPrice());
    }
}
