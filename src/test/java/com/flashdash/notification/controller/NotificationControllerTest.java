package com.flashdash.notification.controller;

import com.flashdash.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificationControllerTest {

    @Autowired
    private NotificationController notificationController;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void testSendAccountConfirmationEmail() {
        // Given
        String token = "testToken";
        doNothing().when(notificationService).sendAccountConfirmationEmail(token);

        // When
        ResponseEntity<Void> response = notificationController.sendAccountConfirmationEmail(token);

        // Then
        verify(notificationService, times(1)).sendAccountConfirmationEmail(token);
        assertEquals(HttpStatus.ACCEPTED.value(), response.getStatusCodeValue());
    }

    @Test
    void testSendFriendInviteEmail() {
        // Given
        doNothing().when(notificationService).sendFriendInviteEmail();

        // When
        ResponseEntity<Void> response = notificationController.sendFriendInviteEmail();

        // Then
        verify(notificationService, times(1)).sendFriendInviteEmail();
        assertEquals(HttpStatus.ACCEPTED.value(), response.getStatusCodeValue());
    }

    @Test
    void testFriendInvitationAcceptedEmail() {
        // Given
        doNothing().when(notificationService).sendFriendAcceptedEmail();

        // When
        ResponseEntity<Void> response = notificationController.sendFriendAcceptedEmail();

        // Then
        verify(notificationService, times(1)).sendFriendAcceptedEmail();
        assertEquals(HttpStatus.ACCEPTED.value(), response.getStatusCodeValue());
    }

    @Test
    void testEnableDailyNotifications_DefaultTime() {
        // Given
        doNothing().when(notificationService).enableDailyNotifications(null);

        // When
        ResponseEntity<Void> response = notificationController.enableDailyNotifications(null);

        // Then
        verify(notificationService, times(1)).enableDailyNotifications(null);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void testEnableDailyNotifications_CustomTime() {
        // Given
        LocalTime customTime = LocalTime.of(14, 30);
        doNothing().when(notificationService).enableDailyNotifications(customTime);

        // When
        ResponseEntity<Void> response = notificationController.enableDailyNotifications(customTime);

        // Then
        verify(notificationService, times(1)).enableDailyNotifications(customTime);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void testDisableDailyNotifications() {
        // Given
        doNothing().when(notificationService).disableDailyNotifications();

        // When
        ResponseEntity<Void> response = notificationController.disableDailyNotifications();

        // Then
        verify(notificationService, times(1)).disableDailyNotifications();
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }
}
