package com.ecommerce.services;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;

    public void sendEmail(String recipient, String subject, String content) {

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom("kira3092650@gmail.com");
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setContent(content, "text/html");
        } catch (MessagingException e) {
            log.error(e.toString());
        }

        javaMailSender.send(message);
        System.out.println("Mail send successfully...");

    }
}
