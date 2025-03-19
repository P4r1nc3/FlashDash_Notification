package com.flashdash.notification.service;

import com.flashdash.notification.exception.ErrorCode;
import com.flashdash.notification.exception.FlashDashException;
import com.flashdash.notification.model.Subscriber;
import com.flashdash.notification.repository.SubscriberRepository;
import com.flashdash.notification.util.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    private NotificationSchedulerService notificationSchedulerService;

    @MockitoBean
    private SubscriberRepository subscriberRepository;

    @Value("${base.url}")
    private String baseUrl;

    private Subscriber subscriber;

    @BeforeEach
    void setUp() {
        subscriber = new Subscriber();
        subscriber.setUserFrn("user-123");
        subscriber.setEmail("user@example.com");
        subscriber.setCreatedAt(LocalDateTime.now());
        subscriber.setUpdatedAt(LocalDateTime.now());
        subscriber.setNotificationTime(LocalDateTime.now());
        subscriber.setDailyNotifications(false);
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
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService, times(1)).sendEmail(
                eq(email),
                eq("✅ Confirm Your FlashDash Account"),
                contentCaptor.capture()
        );

        String emailContent = contentCaptor.getValue();
        assertThat(emailContent).contains("Welcome to FlashDash!");
        assertThat(emailContent).contains("activate?token=" + token);
        assertThat(emailContent).contains("class='button'");
        assertThat(emailContent).contains("style='color: white !important;'");
        assertThat(emailContent).contains("Activate Account");
    }

    @Test
    void testSendFriendInviteEmail() {
        // Given
        String email = "user@example.com";
        when(userContext.getUserEmail()).thenReturn(email);

        // When
        notificationService.sendFriendInviteEmail();

        // Then
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService, times(1)).sendEmail(
                eq(email),
                eq("🎉 New Friend Invitation on FlashDash!"),
                contentCaptor.capture()
        );

        String emailContent = contentCaptor.getValue();
        assertThat(emailContent).contains("You've Received a Friend Request!");
        assertThat(emailContent).contains("/friends");
        assertThat(emailContent).contains("class='button'");
        assertThat(emailContent).contains("style='color: white !important;'");
        assertThat(emailContent).contains("View Friend Request");
    }


    @Test
    void testSendFriendAcceptedEmail() {
        // Given
        String email = "user@example.com";
        when(userContext.getUserEmail()).thenReturn(email);

        // When
        notificationService.sendFriendAcceptedEmail();

        // Then
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService, times(1)).sendEmail(
                eq(email),
                eq("🤝 Friend Request Accepted on FlashDash!"),
                contentCaptor.capture()
        );

        String emailContent = contentCaptor.getValue();
        assertThat(emailContent).contains("Friend Request Accepted!");
        assertThat(emailContent).contains("/friends");
        assertThat(emailContent).contains("class='button'");
        assertThat(emailContent).contains("style='color: white !important;'");
        assertThat(emailContent).contains("View Friends");
        assertThat(emailContent).contains("Start collaborating and boost your learning experience");
    }

    @Test
    void testEnableDailyNotifications_DefaultTime() {
        // Given
        when(userContext.getUserFrn()).thenReturn(subscriber.getUserFrn());
        when(subscriberRepository.findByUserFrn(subscriber.getUserFrn())).thenReturn(Optional.of(subscriber));

        // When
        notificationService.enableDailyNotifications(null);

        // Then
        verify(subscriberRepository, times(1)).save(subscriber);
        verify(notificationSchedulerService, times(1)).cancelScheduledNotification(subscriber.getUserFrn());
        verify(notificationSchedulerService, times(1)).scheduleNotification(subscriber);

        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService, times(1)).sendEmail(
                eq(subscriber.getEmail()),
                eq("🔔 Daily Notifications Enabled!"),
                contentCaptor.capture()
        );

        String emailContent = contentCaptor.getValue();
        assertThat(emailContent).contains("Daily Reminders Activated");
        assertThat(emailContent).contains("<strong>08:00</strong>");
        assertThat(emailContent).contains("class='button'");
        assertThat(emailContent).contains("style='color: white !important;'");
        assertThat(emailContent).contains("Manage Notifications");
        assertThat(subscriber.isDailyNotifications()).isTrue();
    }

    @Test
    void testEnableDailyNotifications_CustomTime() {
        // Given
        when(userContext.getUserFrn()).thenReturn(subscriber.getUserFrn());
        when(subscriberRepository.findByUserFrn(subscriber.getUserFrn())).thenReturn(Optional.of(subscriber));

        LocalTime customTime = LocalTime.of(14, 30);

        // When
        notificationService.enableDailyNotifications(customTime);

        // Then
        verify(subscriberRepository, times(1)).save(subscriber);
        verify(notificationSchedulerService, times(1)).cancelScheduledNotification(subscriber.getUserFrn());
        verify(notificationSchedulerService, times(1)).scheduleNotification(subscriber);

        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService, times(1)).sendEmail(
                eq(subscriber.getEmail()),
                eq("🔔 Daily Notifications Enabled!"),
                contentCaptor.capture()
        );

        String emailContent = contentCaptor.getValue();
        assertThat(emailContent).contains("Daily Reminders Activated");
        assertThat(emailContent).contains("<strong>14:30</strong>");
        assertThat(emailContent).contains("/account");
        assertThat(emailContent).contains("class='button'");
        assertThat(emailContent).contains("style='color: white !important;'");
        assertThat(subscriber.isDailyNotifications()).isTrue();

        // Verify that the notification time was set correctly
        LocalTime setTime = subscriber.getNotificationTime().toLocalTime();
        assertThat(setTime.getHour()).isEqualTo(customTime.getHour());
        assertThat(setTime.getMinute()).isEqualTo(customTime.getMinute());
    }

    @Test
    void testEnableDailyNotifications_UserNotFound() {
        // Given
        when(userContext.getUserFrn()).thenReturn("unknown-user");
        when(subscriberRepository.findByUserFrn("unknown-user")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> notificationService.enableDailyNotifications(null))
                .isInstanceOf(FlashDashException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.E404001)
                .hasMessage("User not found.");

        verify(subscriberRepository, never()).save(any());
        verify(notificationSchedulerService, never()).scheduleNotification(any());
        verify(emailService, never()).sendEmail(any(), any(), any());
    }

    @Test
    void testDisableDailyNotifications() {
        // Given
        when(userContext.getUserFrn()).thenReturn(subscriber.getUserFrn());
        when(subscriberRepository.findByUserFrn(subscriber.getUserFrn())).thenReturn(Optional.of(subscriber));

        // When
        notificationService.disableDailyNotifications();

        // Then
        verify(subscriberRepository, times(1)).save(subscriber);
        verify(notificationSchedulerService, times(1)).cancelScheduledNotification(subscriber.getUserFrn());

        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService, times(1)).sendEmail(
                eq(subscriber.getEmail()),
                eq("🔕 Daily Notifications Disabled"),
                contentCaptor.capture()
        );

        String emailContent = contentCaptor.getValue();
        assertThat(emailContent).contains("Daily Reminders Paused");
        assertThat(emailContent).contains("You've successfully disabled daily learning reminders");
        assertThat(emailContent).contains("/account");
        assertThat(emailContent).contains("class='button'");
        assertThat(emailContent).contains("style='color: white !important;'");
        assertThat(emailContent).contains("Manage Notifications");
        assertThat(subscriber.isDailyNotifications()).isFalse();
    }

    @Test
    void testDisableDailyNotifications_UserNotFound() {
        // Given
        when(userContext.getUserFrn()).thenReturn("unknown-user");
        when(subscriberRepository.findByUserFrn("unknown-user")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> notificationService.disableDailyNotifications())
                .isInstanceOf(FlashDashException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.E404001)
                .hasMessage("User not found.");

        verify(subscriberRepository, never()).save(any());
        verify(notificationSchedulerService, never()).cancelScheduledNotification(any());
        verify(emailService, never()).sendEmail(any(), any(), any());
    }
}