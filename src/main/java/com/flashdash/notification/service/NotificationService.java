package com.flashdash.notification.service;

import com.flashdash.notification.model.NotificationChannel;
import com.flashdash.notification.model.NotificationSubscriber;
import com.flashdash.notification.repository.NotificationSubscriberRepository;
import com.flashdash.notification.util.UserContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final UserContext userContext;
    private final EmailService emailService;
    private final NotificationSubscriberRepository repository;

    @Value("${base.url}")
    private String baseUrl;

    public NotificationService(UserContext userContext,
                               EmailService emailService,
                               NotificationSubscriberRepository repository) {
        this.repository = repository;
        this.userContext = userContext;
        this.emailService = emailService;
    }

    public void registerUser() {
        String userFrn = userContext.getUserFrn();
        String email = userContext.getUserEmail();

        if (repository.existsByUserFrn(userFrn)) {
            throw new IllegalStateException("User already registered.");
        }

        NotificationSubscriber subscriber = new NotificationSubscriber();
        subscriber.setUserFrn(userFrn);
        subscriber.setEmail(email);
        subscriber.setCreatedAt(LocalDateTime.now());
        subscriber.setUpdatedAt(LocalDateTime.now());
        subscriber.setNotificationTime(LocalDateTime.now());
        subscriber.setDailyNotifications(true);
        subscriber.setNotificationChannel(NotificationChannel.EMAIL);

        repository.save(subscriber);
    }

    public void unregisterUser() {
        String userFrn = userContext.getUserFrn();

        if (!repository.existsByUserFrn(userFrn)) {
            throw new IllegalStateException("User not found.");
        }
        repository.deleteById(userFrn);
    }

    public void sendAccountConfirmationEmail(String token) {
        String confirmationLink = baseUrl + "/auth/activate?token=" + token;
        String email = userContext.getUserEmail();
        String subject = "Confirm Your FlashDash Account";
        String content = "<strong>Welcome to FlashDash!</strong><br><br>" +
                "Click the button below to activate your account:<br><br>" +
                "<a href='" + confirmationLink + "' class='button'>Activate Account</a>";

        emailService.sendEmail(email, subject, content);
    }

    public void sendFriendInviteEmail() {
        String email = userContext.getUserEmail();
        String subject = "🎉 New Friend Invitation on FlashDash!";
        String content = "<strong>You have a new friend request!</strong><br><br>" +
                "Someone has sent you a friend invitation. Click below to check your invites:<br><br>" +
                "<a href='" + baseUrl + "' class='button'>Go to FlashDash</a>";

        emailService.sendEmail(email, subject, content);
    }

    public void enableDailyNotifications() {
        String email = userContext.getUserEmail();
        String subject = "🔔 Daily Notifications Enabled!";
        String content = "<strong>Great news!</strong><br><br>" +
                "You have successfully enabled daily learning reminders. Stay on track with your goals!<br><br>" +
                "<a href='" + baseUrl + "' class='button'>Go to FlashDash</a>";

        emailService.sendEmail(email, subject, content);
    }

    public void disableDailyNotifications() {
        String email = userContext.getUserEmail();
        String subject = "🔕 Daily Notifications Disabled";
        String content = "<strong>You have disabled daily reminders.</strong><br><br>" +
                "You will no longer receive learning notifications. You can re-enable them anytime.<br><br>" +
                "<a href='" + baseUrl + "' class='button'>Go to FlashDash</a>";

        emailService.sendEmail(email, subject, content);
    }
}
