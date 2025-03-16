package com.flashdash.notification.service;

import com.flashdash.notification.exception.ErrorCode;
import com.flashdash.notification.exception.FlashDashException;
import com.flashdash.notification.model.NotificationSubscriber;
import com.flashdash.notification.repository.NotificationSubscriberRepository;
import com.flashdash.notification.util.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
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

    @MockitoBean
    private NotificationSubscriberRepository repository;

    @Test
    void testRegisterUser_Success() {
        // Given
        String userFrn = "user-123";
        String email = "user@example.com";
        when(userContext.getUserFrn()).thenReturn(userFrn);
        when(userContext.getUserEmail()).thenReturn(email);
        when(repository.existsByUserFrn(userFrn)).thenReturn(false);

        // When
        notificationService.registerUser();

        // Then
        verify(repository, times(1)).save(any(NotificationSubscriber.class));
    }

    @Test
    void testRegisterUser_AlreadyRegistered() {
        // Given
        String userFrn = "user-123";
        when(userContext.getUserFrn()).thenReturn(userFrn);
        when(repository.existsByUserFrn(userFrn)).thenReturn(true);

        // When & Then
        FlashDashException exception = assertThrows(
                FlashDashException.class,
                () -> notificationService.registerUser()
        );

        assertEquals(ErrorCode.E409001, exception.getErrorCode());
        assertEquals("User already registered.", exception.getMessage());
        verify(repository, never()).save(any(NotificationSubscriber.class));
    }

    @Test
    void testUnregisterUser_Success() {
        // Given
        String userFrn = "user-456";
        when(userContext.getUserFrn()).thenReturn(userFrn);
        when(repository.existsByUserFrn(userFrn)).thenReturn(true);

        // When
        notificationService.unregisterUser();

        // Then
        verify(repository, times(1)).deleteByUserFrn(userFrn);
    }

    @Test
    void testUnregisterUser_NotFound() {
        // Given
        String userFrn = "user-789";
        when(userContext.getUserFrn()).thenReturn(userFrn);
        when(repository.existsByUserFrn(userFrn)).thenReturn(false);

        // When & Then
        FlashDashException exception = assertThrows(
                FlashDashException.class,
                () -> notificationService.unregisterUser()
        );

        assertEquals(ErrorCode.E404001, exception.getErrorCode());
        assertEquals("User not found.", exception.getMessage());
        verify(repository, never()).deleteByUserFrn(anyString());
    }

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
