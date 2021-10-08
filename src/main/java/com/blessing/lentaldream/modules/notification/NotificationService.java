package com.blessing.lentaldream.modules.notification;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void createNotification(Post post, Account account, NotificationType notificationType) {
        Notification notification = Notification.createNewNotification(post,account,notificationType);
        notificationRepository.save(notification);
    }

    public List<Notification> loadNewNotification(Account account){
        List<Notification> notifications = notificationRepository.findNotificationsByAccountAndCheckedOrderByCreatedDateTimeDesc(account, false);
        markAsRead(notifications);
        return notifications;
    }

    public List<Notification> loadAllNotification(Account account) {
        return notificationRepository.findAllByAccountOrderByCreatedDateTimeDesc(account);
    }

    private void markAsRead(List<Notification> notifications) {
        notifications.forEach(Notification::markAsRead);
    }


    public void deleteAllNotification(Account account) {
        notificationRepository.deleteByAccountAndChecked(account,true);
    }
}
