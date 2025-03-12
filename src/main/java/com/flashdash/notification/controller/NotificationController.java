package com.flashdash.notification.controller;

import com.flashdash.notification.service.NotificationService;
import com.flashdash.notification.util.UserContext;
import com.p4r1nc3.flashdash.notification.model.AccountConfirmationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<String> sendAccountConfirmationEmail(@RequestBody AccountConfirmationRequest request) {
        notificationService.sendAccountConfirmationEmail(request);
        return ResponseEntity.accepted().body("Confirmation email sent successfully");
    }

    @PostMapping("/friend-invite")
    public ResponseEntity<String> sendFriendInviteEmail(@RequestParam String recipientEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String senderEmail = authentication.getName();
        notificationService.sendFriendInviteEmail(recipientEmail, senderEmail);
        return ResponseEntity.accepted().body("Friend invitation email sent successfully");
    }

    @PostMapping("/daily/enable")
    public ResponseEntity<String> enableDailyNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        notificationService.enableDailyNotifications(userEmail);
        return ResponseEntity.ok("Daily notifications enabled successfully");
    }

    @PostMapping("/daily/disable")
    public ResponseEntity<String> disableDailyNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        notificationService.disableDailyNotifications(userEmail);
        return ResponseEntity.ok("Daily notifications disabled successfully");
    }
}
