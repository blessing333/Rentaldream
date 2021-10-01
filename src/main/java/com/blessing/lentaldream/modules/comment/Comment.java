package com.blessing.lentaldream.modules.comment;

import com.blessing.lentaldream.modules.account.UserAccount;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.post.domain.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)

public class Comment {
    @Id @GeneratedValue @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account writer;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    private LocalDateTime createdDate;

    @Lob
    private String content;

    public static Comment createNewComment(Account account, Post post, String content){
        Comment instance = new Comment();
        instance.setWriter(account);
        instance.setPost(post);
        instance.setContent(content);
        instance.setCreatedDate(LocalDateTime.now());
        return instance;
    }
    public boolean isWriter(UserAccount userAccount){
        Account account = userAccount.getAccount();
        return writer.equals(account);
    }

    public boolean isWriter(Account account){
        return writer.equals(account);
    }
}
