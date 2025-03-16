package com.flashdash.notification.service;

import com.flashdash.notification.exception.ErrorCode;
import com.flashdash.notification.exception.FlashDashException;
import com.flashdash.notification.model.NotificationChannel;
import com.flashdash.notification.model.Subscriber;
import com.flashdash.notification.repository.SubscriberRepository;
import com.flashdash.notification.util.UserContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class SubscriberService {

    private final UserContext userContext;
    private final SubscriberRepository repository;

    public SubscriberService(UserContext userContext, SubscriberRepository repository) {
        this.repository = repository;
        this.userContext = userContext;
    }

    public void registerUser() {
        String userFrn = userContext.getUserFrn();
        String email = userContext.getUserEmail();

        if (repository.existsByUserFrn(userFrn)) {
            throw new FlashDashException(ErrorCode.E409001, "User already registered.");
        }

        Subscriber subscriber = new Subscriber();
        subscriber.setUserFrn(userFrn);
        subscriber.setEmail(email);
        subscriber.setCreatedAt(LocalDateTime.now());
        subscriber.setUpdatedAt(LocalDateTime.now());
        subscriber.setNotificationTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)));
        subscriber.setDailyNotifications(true);
        subscriber.setNotificationChannel(NotificationChannel.EMAIL);

        repository.save(subscriber);
    }

    public Subscriber getSubscriberDetails() {
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
