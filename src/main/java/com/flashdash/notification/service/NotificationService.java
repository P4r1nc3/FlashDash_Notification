package com.flashdash.notification.service;

import com.flashdash.notification.exception.ErrorCode;
import com.flashdash.notification.exception.FlashDashException;
import com.flashdash.notification.model.Subscriber;
import com.flashdash.notification.repository.SubscriberRepository;
import com.flashdash.notification.util.UserContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class NotificationService {

    private final UserContext userContext;
    private final EmailService emailService;
    private final NotificationSchedulerService notificationSchedulerService;
    private final SubscriberRepository subscriberRepository;

    @Value("${base.url}")
    private String baseUrl;

    public NotificationService(UserContext userContext,
                               EmailService emailService,
                               NotificationSchedulerService notificationSchedulerService,
                               SubscriberRepository subscriberRepository) {
        this.userContext = userContext;
        this.emailService = emailService;
        this.notificationSchedulerService = notificationSchedulerService;
        this.subscriberRepository = subscriberRepository;
    }

    public void sendAccountConfirmationEmail(String token) {
        String confirmationLink = baseUrl + "/auth/activate?token=" + token;
        String email = userContext.getUserEmail();
        String subject = "✅ Confirm Your FlashDash Account";
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

    @Transactional
    public void enableDailyNotifications(LocalTime notificationTime) {
        String userFrn = userContext.getUserFrn();
        Subscriber subscriber = subscriberRepository.findByUserFrn(userFrn)
                .orElseThrow(() -> new FlashDashException(ErrorCode.E404001, "User not found."));

        if (notificationTime == null) {
            notificationTime = LocalTime.of(8, 0);
        }

        subscriber.setDailyNotifications(true);
        subscriber.setNotificationTime(LocalDateTime.now().with(notificationTime));
        subscriberRepository.save(subscriber);

        notificationSchedulerService.cancelScheduledNotification(userFrn);
        notificationSchedulerService.scheduleNotification(subscriber);

        String email = subscriber.getEmail();
        String subject = "🔔 Daily Notifications Enabled!";
        String content = "<strong>Great news!</strong><br><br>" +
                "You have successfully enabled daily learning reminders for <strong>" + notificationTime + "</strong>. Stay on track with your goals!<br><br>" +
                "<a href='" + baseUrl + "' class='button'>Go to FlashDash</a>";

        emailService.sendEmail(email, subject, content);
    }

    @Transactional
    public void disableDailyNotifications() {
        String userFrn = userContext.getUserFrn();
        Subscriber subscriber = subscriberRepository.findByUserFrn(userFrn)
                .orElseThrow(() -> new FlashDashException(ErrorCode.E404001, "User not found."));

        subscriber.setDailyNotifications(false);
        subscriberRepository.save(subscriber);
        notificationSchedulerService.cancelScheduledNotification(userFrn);

        String email = subscriber.getEmail();
        String subject = "🔕 Daily Notifications Disabled";
        String content = "<strong>You have disabled daily reminders.</strong><br><br>" +
                "You will no longer receive learning notifications. You can re-enable them anytime.<br><br>" +
                "<a href='" + baseUrl + "' class='button'>Go to FlashDash</a>";

        emailService.sendEmail(email, subject, content);
    }
}
