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
        String confirmationLink = baseUrl + "/login?token=" + token;
        String email = userContext.getUserEmail();
        String subject = "✅ Confirm Your FlashDash Account";
        String content = "<h2>Welcome to FlashDash!</h2>" +
                "<p>Thank you for signing up. To start learning with FlashDash, please activate your account by clicking the button below.</p>" +
                "<p><a href='" + confirmationLink + "' class='button' style='color: white !important;'>Activate Account</a></p>" +
                "<p>If the button doesn't work, you can also copy and paste the following link into your browser:</p>" +
                "<p style='font-size: 14px; color: #6b7280;'>" + confirmationLink + "</p>";

        emailService.sendEmail(email, subject, content);
    }

    public void sendAchievementUnlockedEmail() {
        String email = userContext.getUserEmail();
        String subject = "🏆 Achievement Unlocked on FlashDash!";
        String content = "<h2>Congratulations! You've Unlocked an Achievement!</h2>" +
                "<p>Your dedication to learning is paying off. Keep up the great work and unlock more achievements!</p>" +
                "<p><a href='" + baseUrl + "/account' class='button' style='color: white !important;'>View Your Achievements</a></p>" +
                "<p>Every achievement is a step toward mastery. We're excited to see your progress!</p>";

        emailService.sendEmail(email, subject, content);
    }

    public void sendFriendInviteEmail() {
        String email = userContext.getUserEmail();
        String subject = "🎉 New Friend Invitation on FlashDash!";
        String content = "<h2>You've Received a Friend Request!</h2>" +
                "<p>Someone wants to connect with you on FlashDash. Adding friends helps you share decks and study together.</p>" +
                "<p><a href='" + baseUrl + "/friends' class='button' style='color: white !important;'>View Friend Request</a></p>" +
                "<p>Happy learning together!</p>";

        emailService.sendEmail(email, subject, content);
    }

    public void sendFriendAcceptedEmail() {
        String email = userContext.getUserEmail();
        String subject = "🤝 Friend Request Accepted on FlashDash!";
        String content = "<h2>Friend Request Accepted!</h2>" +
                "<p>Great news! Someone has accepted your friend request on FlashDash. You can now share decks and study together.</p>" +
                "<p><a href='" + baseUrl + "/friends' class='button' style='color: white !important;'>View Friends</a></p>" +
                "<p>Start collaborating and boost your learning experience!</p>";

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
        String content = "<h2>Daily Reminders Activated</h2>" +
                "<p>You've successfully enabled daily learning reminders at <strong>" + notificationTime + "</strong>.</p>" +
                "<p>Consistent practice is key to effective learning. We'll send you a friendly reminder each day to help you stay on track with your goals.</p>" +
                "<p><a href='" + baseUrl + "/account' class='button' style='color: white !important;'>Manage Notifications</a></p>" +
                "<p>Keep up the great work!</p>";

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
        String content = "<h2>Daily Reminders Paused</h2>" +
                "<p>You've successfully disabled daily learning reminders. You won't receive notifications until you choose to enable them again.</p>" +
                "<p>You can re-enable notifications at any time from your account settings.</p>" +
                "<p><a href='" + baseUrl + "/account' class='button' style='color: white !important;'>Manage Notifications</a></p>" +
                "<p>We're still here whenever you're ready to continue your learning journey!</p>";

        emailService.sendEmail(email, subject, content);
    }
}
