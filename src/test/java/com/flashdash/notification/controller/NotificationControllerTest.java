package com.flashdash.notification.controller;

import com.flashdash.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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
    void testRegisterUser() {
        // Given
        doNothing().when(notificationService).registerUser();

        // When
        ResponseEntity<Void> response = notificationController.registerUser();

        // Then
        verify(notificationService, times(1)).registerUser();
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
    }

    @Test
    void testUnregisterUser() {
        // Given
        doNothing().when(notificationService).unregisterUser();

        // When
        ResponseEntity<Void> response = notificationController.unregisterUser();

        // Then
        verify(notificationService, times(1)).unregisterUser();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
    }

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
    void testEnableDailyNotifications() {
        // Given
        doNothing().when(notificationService).enableDailyNotifications();

        // When
        ResponseEntity<Void> response = notificationController.enableDailyNotifications();

        // Then
        verify(notificationService, times(1)).enableDailyNotifications();
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
