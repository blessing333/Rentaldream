package com.blessing.rentaldream.infra.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

//@Profile({"dev","prod"})
@Profile({"real"})
@Component
@RequiredArgsConstructor
@Slf4j
public class HtmlEmailService implements EMailService {
    private final JavaMailSender javaMailSender;
    @Override
    public void sendEmail(EmailMessage message) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper =new MimeMessageHelper(mimeMessage,false,"UTF-8");
            mimeMessageHelper.setTo(message.getTo());
            mimeMessageHelper.setSubject(message.getSubject());
            mimeMessageHelper.setText(message.getMessage(),true);
            javaMailSender.send(mimeMessage);
            log.info("send mail: {}",message.getMessage());

        } catch (MessagingException e) {
            log.error("mail send error",e);
            throw new RuntimeException(e);
        }
    }
}
