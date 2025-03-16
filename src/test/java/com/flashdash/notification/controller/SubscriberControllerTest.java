package com.flashdash.notification.controller;

import com.flashdash.notification.exception.FlashDashException;
import com.flashdash.notification.model.NotificationChannel;
import com.flashdash.notification.model.Subscriber;
import com.flashdash.notification.service.SubscriberService;
import com.p4r1nc3.flashdash.notification.model.NotificationSubscriber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static com.flashdash.notification.exception.ErrorCode.E404001;
import static com.flashdash.notification.exception.ErrorCode.E409001;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubscriberControllerTest {

    @Autowired
    private SubscriberController subscriberController;

    @MockitoBean
    private SubscriberService subscriberService;

    @Test
    void testRegisterUser() {
        // Given
        doNothing().when(subscriberService).registerUser();

        // When
        ResponseEntity<Void> response = subscriberController.registerUser();

        // Then
        verify(subscriberService, times(1)).registerUser();
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
    }

    @Test
    void testRegisterUserAlreadyExists() {
        // Given
        doThrow(new FlashDashException(E409001, "User already registered."))
                .when(subscriberService).registerUser();

        // When & Then
        FlashDashException exception = assertThrows(FlashDashException.class, () -> subscriberController.registerUser());
        assertEquals("User already registered.", exception.getMessage());
    }

    @Test
    void testUnregisterUser() {
        // Given
        doNothing().when(subscriberService).unregisterUser();

        // When
        ResponseEntity<Void> response = subscriberController.unregisterUser();

        // Then
        verify(subscriberService, times(1)).unregisterUser();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
    }

    @Test
    void testUnregisterUserNotFound() {
        // Given
        doThrow(new FlashDashException(E404001, "User not found."))
                .when(subscriberService).unregisterUser();

        // When & Then
        FlashDashException exception = assertThrows(FlashDashException.class, () -> subscriberController.unregisterUser());
        assertEquals("User not found.", exception.getMessage());
    }

    @Test
    void testGetSubscriberDetails() {
        // Given
        Subscriber mockSubscriber = new Subscriber();
        mockSubscriber.setUserFrn("testFrn");
        mockSubscriber.setEmail("test@example.com");
        mockSubscriber.setNotificationTime(LocalDateTime.of(2025, 3, 15, 8, 0)); // 8:00 AM
        mockSubscriber.setDailyNotifications(true);
        mockSubscriber.setNotificationChannel(NotificationChannel.EMAIL);

        when(subscriberService.getSubscriberDetails()).thenReturn(mockSubscriber);

        // When
        ResponseEntity<NotificationSubscriber> response = subscriberController.getSubscriberDetails();

        // Then
        verify(subscriberService, times(1)).getSubscriberDetails();
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

        NotificationSubscriber expected = new NotificationSubscriber();
        expected.setUserFrn(mockSubscriber.getUserFrn());
        expected.setEmail(mockSubscriber.getEmail());
        expected.setNotificationTime("2025-03-15T08:00:00");
        expected.setDailyNotifications(mockSubscriber.isDailyNotifications());
        expected.setNotificationChannel(NotificationSubscriber.NotificationChannelEnum.EMAIL);

        assertEquals(expected.getUserFrn(), response.getBody().getUserFrn());
        assertEquals(expected.getEmail(), response.getBody().getEmail());
        assertEquals(expected.getDailyNotifications(), response.getBody().getDailyNotifications());
        assertEquals(expected.getNotificationChannel(), response.getBody().getNotificationChannel());
    }

    @Test
    void testGetSubscriberDetailsNotFound() {
        // Given
        doThrow(new FlashDashException(E404001, "User not found."))
                .when(subscriberService).getSubscriberDetails();

        // When & Then
        FlashDashException exception = assertThrows(FlashDashException.class, () -> subscriberController.getSubscriberDetails());
        assertEquals("User not found.", exception.getMessage());
    }
}
