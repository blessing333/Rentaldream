package com.blessing.lentaldream.modules.post.event;

import com.blessing.lentaldream.infra.config.AppProperties;
import com.blessing.lentaldream.infra.mail.EMailService;
import com.blessing.lentaldream.infra.mail.EmailMessage;
import com.blessing.lentaldream.modules.account.domain.Account;
import com.blessing.lentaldream.modules.account.repository.AccountRepository;
import com.blessing.lentaldream.modules.post.domain.Post;
import com.blessing.lentaldream.modules.tag.Tag;
import com.blessing.lentaldream.modules.zone.Zone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;


@Slf4j
@Async
@Component
@RequiredArgsConstructor
public class PostEventListener {
    private final AccountRepository accountRepository;
    private final EMailService eMailService;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;

    @EventListener
    public void handlePostCreatedEvent(PostCreatedEvent postCreatedEvent){
        Post post = postCreatedEvent.getPost();
        List<Tag> tagList = post.getTagsAsTagList(post);
        List<Zone> zoneList = post.getZonesAsZoneList(post);
        List<Account> accounts = accountRepository.findAccountWithTagAndZone(tagList,zoneList);
        accounts.forEach(account -> {
            if (account.isReceivePostCreatedNotificationByEmail()) {
                sendStudyCreatedEmail(post, account, "관심 상품이 등록되었어요!",
                        "빌려드림, '" + post.getTitle() + "' 관심 상품이 등록되었습니다!.");
            }
        });
    }

    private void sendStudyCreatedEmail(Post post, Account account, String contextMessage, String emailSubject) {
        Context context = new Context();
        context.setVariable("nickname", account.getNickname());
        context.setVariable("link", "/post/" + post.getId());
        context.setVariable("linkName", post.getTitle());
        context.setVariable("message", contextMessage);
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .subject(emailSubject)
                .to(account.getEmail())
                .message(message)
                .build();
        eMailService.sendEmail(emailMessage);
    }
}
