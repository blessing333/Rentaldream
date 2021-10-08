package com.blessing.lentaldream.modules.notification;

import com.blessing.lentaldream.modules.account.CurrentUser;
import com.blessing.lentaldream.modules.account.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.blessing.lentaldream.infra.config.UrlConfig.*;
import static com.blessing.lentaldream.infra.config.ViewNameConfig.NOTIFICATION_ALL_VIEW;
import static com.blessing.lentaldream.infra.config.ViewNameConfig.NOTIFICATION_NEW_VIEW;

@Controller
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping(NOTIFICATION_LIST_URL)
    public String createNewNotificationListView(@CurrentUser Account account, Model model){
        List<Notification> notifications = notificationService.loadNewNotification(account);
        model.addAttribute("notifications",notifications);
        return NOTIFICATION_NEW_VIEW;
    }

    @GetMapping(NOTIFICATION_ALL_URL)
    public String createAllNotificationListView(@CurrentUser Account account, Model model){
        List<Notification> notifications = notificationService.loadAllNotification(account);
        model.addAttribute("notifications",notifications);
        return NOTIFICATION_ALL_VIEW;
    }
    @DeleteMapping(NOTIFICATION_URL)
    public String deleteAllNotification(@CurrentUser Account account){
        notificationService.deleteAllNotification(account);
        return REDIRECT_URL + NOTIFICATION_ALL_URL;
    }
}
