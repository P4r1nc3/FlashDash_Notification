package com.flashdash.notification.service;

import com.flashdash.notification.model.Subscriber;
import com.flashdash.notification.repository.SubscriberRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class NotificationSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationSchedulerService.class);

    private final SubscriberRepository subscriberRepository;
    private final EmailService emailService;
    private final TaskScheduler taskScheduler;
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Value("${base.url}")
    private String baseUrl;

    public NotificationSchedulerService(SubscriberRepository subscriberRepository, EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.emailService = emailService;
        this.taskScheduler = createScheduler();
    }

    private TaskScheduler createScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.initialize();
        return scheduler;
    }

    @PostConstruct
    public void initializeScheduledTasks() {
        logger.info("Initializing scheduled notifications for all active subscribers...");
        subscriberRepository.findByDailyNotificationsTrue().forEach(this::scheduleNotification);
    }

    public void scheduleNotification(Subscriber subscriber) {
        if (!subscriber.isDailyNotifications()) {
            logger.warn("Skipping scheduling for user {} as daily notifications are disabled.", subscriber.getUserFrn());
            return;
        }

        LocalDateTime notificationTime = subscriber.getNotificationTime();
        if (notificationTime.isBefore(LocalDateTime.now())) {
            notificationTime = notificationTime.plusDays(1);
        }

        cancelScheduledNotification(subscriber.getUserFrn());

        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(
                () -> sendNotification(subscriber),
                notificationTime.atZone(ZoneId.systemDefault()).toInstant()
        );

        scheduledTasks.put(subscriber.getUserFrn(), scheduledTask);
        logger.info("Scheduled notification for user {} at {}", subscriber.getUserFrn(), notificationTime);
    }

    private void sendNotification(Subscriber subscriber) {
        logger.info("Sending scheduled notification to user {}", subscriber.getUserFrn());

        String subject = "🎯 Time for your daily session!";
        String content = "<strong>Hey!</strong><br><br>" +
                "Remember to complete your daily training in FlashDash. Stay consistent!<br><br>" +
                "<a href='" + baseUrl + "' class='button'>Start your session</a>";

        emailService.sendEmail(subscriber.getEmail(), subject, content);

        subscriber.setNotificationTime(subscriber.getNotificationTime().plusDays(1));
        subscriberRepository.save(subscriber);
        scheduleNotification(subscriber);
    }

    public void cancelScheduledNotification(String userFrn) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.remove(userFrn);
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            logger.info("Canceled scheduled notification for user {}", userFrn);
        }
    }
}
