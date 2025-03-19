package com.flashdash.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("flashdashservice@gmail.com");
            helper.setText(buildHtmlEmail(content), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String buildHtmlEmail(String content) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<style>" +
                "@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap');" +
                "* { margin: 0; padding: 0; box-sizing: border-box; }" +
                "body { font-family: 'Poppins', Arial, sans-serif; background-color: #f4f5f8; padding: 20px; color: #4b5563; line-height: 1.6; }" +
                ".email-container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 12px; overflow: hidden; box-shadow: 0px 4px 16px rgba(0, 0, 0, 0.08); }" +
                ".email-header { padding: 20px 30px; border-bottom: 1px solid #e5e7eb; }" +
                ".logo { font-size: 28px; font-weight: 800; letter-spacing: 0.5px; }" +
                ".logo-dark { color: #1f2937; }" +
                ".logo-gradient { background: linear-gradient(90deg, #60a5fa, #818cf8); -webkit-background-clip: text; background-clip: text; color: transparent; }" +
                ".gradient-line { height: 4px; background: linear-gradient(90deg, #60a5fa, #818cf8); }" +
                ".email-body { padding: 30px; }" +
                "p { margin-bottom: 16px; color: #4b5563; font-size: 16px; }" +
                "strong { color: #1f2937; font-weight: 600; }" +
                ".button { display: inline-block; background: linear-gradient(90deg, #60a5fa, #818cf8); color: white !important; font-weight: 600; padding: 12px 24px; text-decoration: none; border-radius: 8px; text-align: center; margin: 16px 0; transition: all 0.3s ease; box-shadow: 0 4px 6px rgba(97, 174, 250, 0.25); }" +
                ".button:hover { transform: translateY(-2px); box-shadow: 0 6px 10px rgba(97, 174, 250, 0.3); color: white !important; }" +
                "a.button { color: white !important; }" +
                ".button * { color: white !important; }" +
                "h2 { color: #1f2937; font-weight: 700; margin-bottom: 20px; }" +
                ".email-footer { padding: 20px 30px; background-color: #f9fafb; color: #9ca3af; font-size: 14px; text-align: center; }" +
                ".email-footer a { color: #6366f1; text-decoration: none; }" +
                "@media only screen and (max-width: 600px) {" +
                "  .email-container { border-radius: 0; }" +
                "  .email-header, .email-body, .email-footer { padding: 20px; }" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='email-container'>" +
                "<div class='email-header'>" +
                "<div class='logo'><span class='logo-dark'>Flash</span><span class='logo-gradient'>Dash</span></div>" +
                "</div>" +
                "<div class='gradient-line'></div>" +
                "<div class='email-body'>" +
                content +
                "</div>" +
                "<div class='email-footer'>" +
                "© 2025 FlashDash. All rights reserved.<br>" +
                "<a href='#'>Privacy Policy</a> • <a href='#'>Terms of Service</a>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}