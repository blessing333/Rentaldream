package com.blessing.lentaldream.modules.post.domain;

import com.blessing.lentaldream.modules.account.domain.Account;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    private int price;
    private LocalDateTime createdDate;

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
    public void addTag(PostTag tag){
        this.getTags().add(tag);
    }

    public void addZone(PostZone postZone) {
        this.getZones().add(postZone);
    }
}
