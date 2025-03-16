package com.flashdash.notification.service;

import com.flashdash.notification.exception.ErrorCode;
import com.flashdash.notification.exception.FlashDashException;
import com.flashdash.notification.model.NotificationChannel;
import com.flashdash.notification.model.NotificationSubscriber;
import com.flashdash.notification.repository.NotificationSubscriberRepository;
import com.flashdash.notification.util.UserContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SubscriberService {

    private final UserContext userContext;
    private final NotificationSubscriberRepository repository;

    public SubscriberService(UserContext userContext, NotificationSubscriberRepository repository) {
        this.repository = repository;
        this.userContext = userContext;
    }

    public void registerUser() {
        String userFrn = userContext.getUserFrn();
        String email = userContext.getUserEmail();

        if (repository.existsByUserFrn(userFrn)) {
            throw new FlashDashException(ErrorCode.E409001, "User already registered.");
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

    public NotificationSubscriber getSubscriberDetails() {
        String userFrn = userContext.getUserFrn();
        return repository.findById(userFrn)
                .orElseThrow(() -> new FlashDashException(ErrorCode.E404001, "User not found."));
    }

    public void unregisterUser() {
        String userFrn = userContext.getUserFrn();

        if (!repository.existsByUserFrn(userFrn)) {
            throw new FlashDashException(ErrorCode.E404001, "User not found.");
        }

        repository.deleteByUserFrn(userFrn);
    }
}
