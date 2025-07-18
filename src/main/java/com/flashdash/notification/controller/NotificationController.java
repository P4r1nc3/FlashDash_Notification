package com.flashdash.notification.controller;

import com.flashdash.notification.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/confirm-account")
    public ResponseEntity<Void> sendAccountConfirmationEmail(@RequestParam String token) {
        notificationService.sendAccountConfirmationEmail(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/achievement-unlocked")
    public ResponseEntity<Void> sendAchievementUnlockedEmail() {
        notificationService.sendAchievementUnlockedEmail();
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/friend-invite")
    public ResponseEntity<Void> sendFriendInviteEmail() {
        notificationService.sendFriendInviteEmail();
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/friend-accepted")
    public ResponseEntity<Void> sendFriendAcceptedEmail() {
        notificationService.sendFriendAcceptedEmail();
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/daily/enable")
    public ResponseEntity<Void> enableDailyNotifications(@RequestParam(required = false) LocalTime notificationTime) {
        notificationService.enableDailyNotifications(notificationTime);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/daily/disable")
    public ResponseEntity<Void> disableDailyNotifications() {
        notificationService.disableDailyNotifications();
        return ResponseEntity.ok().build();
    }
}
