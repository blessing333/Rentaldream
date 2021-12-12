package com.blessing.rentaldream.modules.account.domain;

import com.blessing.rentaldream.modules.post.domain.Post;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"account","post"})
public class WishList {
    @Id @GeneratedValue
    @Column(name = "FAVORIT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
    public static WishList createNewFavorite(Account account, Post post){
        WishList instance = new WishList();
        instance.account = account;
        instance.post = post;
        return instance;
    }
}
