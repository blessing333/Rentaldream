package com.blessing.lentaldream.modules.notification;

import com.blessing.lentaldream.modules.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    long countByAccountAndChecked(Account account, boolean b);

    List<Notification> findNotificationsByAccountAndCheckedOrderByCreatedDateTimeDesc(Account account,boolean checked);
    List<Notification> findAllByAccountOrderByCreatedDateTimeDesc(Account account);
    void deleteByAccountAndChecked(Account account,Boolean bool);
}
