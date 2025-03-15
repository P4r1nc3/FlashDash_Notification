package com.flashdash.notification.service;

import com.flashdash.notification.util.UserContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final UserContext userContext;
    private final EmailService emailService;

    @Value("${base.url}")
    private String baseUrl;

    public NotificationService(UserContext userContext,
                               EmailService emailService) {
        this.userContext = userContext;
        this.emailService = emailService;
    }

    public void sendAccountConfirmationEmail(String token) {
        String confirmationLink = baseUrl + "/auth/activate?token=" + token;
        String email = userContext.getUserEmail();
        String subject = "Account Activation";
        String content = "Click the link to activate your account: " + confirmationLink;
        emailService.sendEmail(email, subject, content);
    }

    public void sendFriendInviteEmail() {
        String email = userContext.getUserEmail();
        String subject = "You have a new Friend Invitation!";
        String content = "Hi! You've received a friend invitation. Visit FlashDash to accept or decline.";
        emailService.sendEmail(email, subject, content);
    }

    public void enableDailyNotifications() {
        String email = userContext.getUserEmail();
        String subject = "Daily Notifications Enabled";
        String content = "You've successfully enabled daily learning reminders!";
        emailService.sendEmail(email, subject, content);
    }

    public void disableDailyNotifications() {
        String email = userContext.getUserEmail();
        String subject = "Daily Notifications Disabled";
        String content = "You've successfully disabled daily reminders.";
        emailService.sendEmail(email, subject, content);
    }
}
