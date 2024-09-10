package com.ayg.trading.service.interfaces;

import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;

public interface EmailService {
    void sendOtpVerificationEmail(String to,String otp) throws MessagingException;

}
