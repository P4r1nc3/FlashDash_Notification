package com.flashdash.notification.controller;

import com.flashdash.notification.model.Subscriber;
import com.flashdash.notification.service.SubscriberService;
import com.p4r1nc3.flashdash.notification.model.NotificationSubscriber;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping
    public ResponseEntity<Void> registerUser() {
        subscriberService.registerUser();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unregisterUser() {
        subscriberService.unregisterUser();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<NotificationSubscriber> getSubscriberDetails() {
        Subscriber subscriber = subscriberService.getSubscriberDetails();
        return ResponseEntity.ok(mapToNotificationSubscriber(subscriber));
    }

    private NotificationSubscriber mapToNotificationSubscriber(Subscriber subscriber) {
        NotificationSubscriber notificationSubscriber = new NotificationSubscriber();
        notificationSubscriber.setUserFrn(subscriber.getUserFrn());
        notificationSubscriber.setEmail(subscriber.getEmail());
        notificationSubscriber.setNotificationTime(subscriber.getNotificationTime().toString()); // Konwersja LocalDateTime -> String
        notificationSubscriber.setDailyNotifications(subscriber.isDailyNotifications());
        notificationSubscriber.setNotificationChannel(
                NotificationSubscriber.NotificationChannelEnum.fromValue(subscriber.getNotificationChannel().name()));

        return notificationSubscriber;
    }
}
