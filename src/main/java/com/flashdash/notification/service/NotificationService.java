package com.flashdash.notification.service;

import com.p4r1nc3.flashdash.notification.model.AccountConfirmationRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendAccountConfirmationEmail(AccountConfirmationRequest request) {
        String subject = "Account Activation";
        String content = "Click the link to activate your account: " + request.getConfirmationLink();
        emailService.sendEmail(request.getEmail(), subject, content);
    }

    public void sendFriendInviteEmail(String recipientEmail, String senderEmail) {
        String subject = "You have a new Friend Invitation!";
        String content = "Hi! You've received a friend invitation from " + senderEmail +
                ". Visit FlashDash to accept or decline.";
        emailService.sendEmail(recipientEmail, subject, content);
    }

    public void enableDailyNotifications(String userEmail) {
        String subject = "Daily Notifications Enabled";
        String content = "You've successfully enabled daily learning reminders!";
        emailService.sendEmail(userEmail, subject, content);
    }

    public void disableDailyNotifications(String userEmail) {
        String subject = "Daily Notifications Disabled";
        String content = "You've successfully disabled daily reminders.";
        emailService.sendEmail(userEmail, subject, content);
    }
}
