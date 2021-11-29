package com.blessing.rentaldream.modules.post.domain;

import com.blessing.rentaldream.modules.account.UserAccount;
import com.blessing.rentaldream.modules.account.domain.Account;
import com.blessing.rentaldream.modules.comment.Comment;
import com.blessing.rentaldream.modules.post.form.PostForm;
import com.blessing.rentaldream.modules.tag.Tag;
import com.blessing.rentaldream.modules.zone.Zone;
import lombok.*;

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

    private String thumbnail;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String description;

    @OneToMany(mappedBy ="post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> tags = new HashSet<>();

    @OneToMany(mappedBy ="post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostZone> zones = new HashSet<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    private int price;
    private LocalDateTime createdDate;
    private int viewCount = 0;
    private int favoriteCount = 0;

    public static Post createNewPost(PostForm postForm,Account account) {
        Post instance = new Post();
        instance.setTitle(postForm.getTitle());
        instance.setCreatedBy(account);
        instance.setDescription(postForm.getDescription());
        instance.setCreatedDate(LocalDateTime.now());
        instance.setPrice(postForm.getPrice());
        instance.setPostStatus(PostStatus.AVAILABLE);
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
        this.setPrice(postForm.getPrice());
    }

    public void modifyThumbnailPath(String thumbnailPath){
        this.setThumbnail(thumbnailPath);
    }

    public List<Tag> getTagsAsTagList(Post post){
        return post.getTags().stream().map(PostTag::getTag).collect(Collectors.toList());
    }

    public List<Zone> getZonesAsZoneList(Post post){
        return getZones().stream().map(PostZone::getZone).collect(Collectors.toList());
    }
}
