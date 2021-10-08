package com.blessing.lentaldream.modules.notification;

import com.blessing.lentaldream.infra.config.UrlConfig;
import com.blessing.lentaldream.modules.account.UserAccount;
import com.blessing.lentaldream.modules.account.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class NotificationInterceptor implements HandlerInterceptor {
    private final NotificationRepository notificationRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(modelAndView != null && !isRedirectView(modelAndView) && authentication != null && authentication.getPrincipal() instanceof UserAccount){
            Account account = ((UserAccount)authentication.getPrincipal()).getAccount();
            long count = notificationRepository.countByAccountAndChecked(account,false);
            modelAndView.addObject("hasNotification",count);
        }
    }

    private boolean isRedirectView(ModelAndView modelAndView) {
        return modelAndView.getViewName().startsWith(UrlConfig.REDIRECT_URL) || modelAndView.getView() instanceof RedirectView;
    }
}
