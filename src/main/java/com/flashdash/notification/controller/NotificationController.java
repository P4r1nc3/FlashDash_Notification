package com.flashdash.notification.controller;

import com.flashdash.notification.service.NotificationService;
import com.flashdash.notification.util.UserContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final UserContext userContext;
    private final NotificationService notificationService;

    public NotificationController(UserContext userContext, NotificationService notificationService) {
        this.userContext = userContext;
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<Void> getTest() {
        System.out.println(userContext.getUserEmail() + " " + userContext.getUserFrn());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm-account")
    public ResponseEntity<Void> sendAccountConfirmationEmail(@RequestParam String token) {
        notificationService.sendAccountConfirmationEmail(token);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/friend-invite")
    public ResponseEntity<Void> sendFriendInviteEmail() {
        notificationService.sendFriendInviteEmail();
        return ResponseEntity.accepted().build();
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
