package com.blessing.lentaldream.modules.favorite;

import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.post.domain.Post;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"account","post"})
public class Favorite {
    @Id @GeneratedValue
    @Column(name = "FAVORIT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
    public static Favorite createNewFavorite(Account account,Post post){
        Favorite instance = new Favorite();
        instance.account = account;
        instance.post = post;
        return instance;
    }
}
