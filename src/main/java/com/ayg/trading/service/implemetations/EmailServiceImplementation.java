package com.ayg.trading.service.implemetations;

import com.ayg.trading.service.interfaces.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImplementation implements EmailService {
    private JavaMailSender mailSender;

    /**
     * @param to
     * @param otp
     */
    @Override
    public void sendOtpVerificationEmail(String to,  String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,"utf-8");
        String subject = "Verify OTP";
        String message = "Your verification code is "+otp;
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(message);
        mimeMessageHelper.setTo(to);

        try {
            mailSender.send(mimeMessage);
        } catch (MailException e) {
            log.error("Error : {}",e.getMessage());
            throw new MailSendException("Send verification mail is failed");
        }


    }
}
