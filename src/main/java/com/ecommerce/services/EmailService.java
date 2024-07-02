package com.ecommerce.services;

public interface EmailService {
    void sendEmail(String recipient, String subject, String content);
}
