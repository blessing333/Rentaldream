package com.blessing.rentaldream.infra.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
@Slf4j
//@Profile({"local", "test"})
@Profile({"dev", "test","prod"})
@Component
public class ConsoleEmailService implements EMailService{
    @Override
    public void sendEmail(EmailMessage emailMessage) {
        log.info("sent email: {}", emailMessage.getMessage());
    }
}
