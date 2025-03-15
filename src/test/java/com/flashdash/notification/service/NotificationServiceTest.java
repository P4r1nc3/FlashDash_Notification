package com.flashdash.notification.service;

import com.flashdash.notification.util.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @MockitoBean
    private UserContext userContext;

    @MockitoBean
    private EmailService emailService;

    @Test
    void testSendAccountConfirmationEmail() {
        // Given
        String token = "testToken";
        String email = "user@example.com";
        when(userContext.getUserEmail()).thenReturn(email);

        // When
        notificationService.sendAccountConfirmationEmail(token);

        // Then
        verify(emailService, times(1)).sendEmail(
                eq(email),
                eq("Confirm Your FlashDash Account"),
                contains("Activate Account")
        );
    }

    @Test
    void testSendFriendInviteEmail() {
        // Given
        String email = "user@example.com";
        when(userContext.getUserEmail()).thenReturn(email);

        // When
        notificationService.sendFriendInviteEmail();

        // Then
        verify(emailService, times(1)).sendEmail(
                eq(email),
                eq("🎉 New Friend Invitation on FlashDash!"),
                contains("Go to FlashDash")
        );
    }

    @Test
    void testEnableDailyNotifications() {
        // Given
        String email = "user@example.com";
        when(userContext.getUserEmail()).thenReturn(email);

        // When
        notificationService.enableDailyNotifications();

        // Then
        verify(emailService, times(1)).sendEmail(
                eq(email),
                eq("🔔 Daily Notifications Enabled!"),
                contains("Go to FlashDash")
        );
    }

    @Test
    void testDisableDailyNotifications() {
        // Given
        String email = "user@example.com";
        when(userContext.getUserEmail()).thenReturn(email);

        // When
        notificationService.disableDailyNotifications();

        // Then
        verify(emailService, times(1)).sendEmail(
                eq(email),
                eq("🔕 Daily Notifications Disabled"),
                contains("Go to FlashDash")
        );
    }
}
