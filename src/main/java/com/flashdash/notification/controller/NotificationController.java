package com.flashdash.notification.controller;

import com.flashdash.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser() {
        notificationService.registerUser();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unregister")
    public ResponseEntity<Void> unregisterUser() {
        notificationService.unregisterUser();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm-account")
    public ResponseEntity<Void> sendAccountConfirmationEmail(@RequestParam String token) {
        notificationService.sendAccountConfirmationEmail(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/friend-invite")
    public ResponseEntity<Void> sendFriendInviteEmail() {
        notificationService.sendFriendInviteEmail();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/daily/enable")
    public ResponseEntity<Void> enableDailyNotifications() {
        notificationService.enableDailyNotifications();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/daily/disable")
    public ResponseEntity<Void> disableDailyNotifications() {
        notificationService.disableDailyNotifications();
        return ResponseEntity.ok().build();
    }
}
