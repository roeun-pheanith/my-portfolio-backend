package com.nith.flex.portfolio.service;


public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
