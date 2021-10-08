package com.blessing.lentaldream.modules.notification;

import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.post.domain.Post;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    private boolean checked;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    private LocalDateTime createdDateTime;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    public void markAsRead(){
        setChecked(true);
    }

    public static Notification createNewNotification(Post post,Account account,NotificationType type){
        Notification instance = new Notification();
        instance.setPost(post);
        instance.setAccount(account);
        instance.setChecked(false);
        instance.createdDateTime = LocalDateTime.now();
        instance.setNotificationType(type);
        return instance;
    }
}
