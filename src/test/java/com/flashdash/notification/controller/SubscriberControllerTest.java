package com.flashdash.notification.controller;

import com.flashdash.notification.exception.FlashDashException;
import com.flashdash.notification.model.NotificationSubscriber;
import com.flashdash.notification.service.SubscriberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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
        NotificationSubscriber mockSubscriber = new NotificationSubscriber();
        mockSubscriber.setUserFrn("testFrn");
        mockSubscriber.setEmail("test@example.com");

        when(subscriberService.getSubscriberDetails()).thenReturn(mockSubscriber);

        // When
        ResponseEntity<?> response = subscriberController.getSubscriberDetails();

        // Then
        verify(subscriberService, times(1)).getSubscriberDetails();
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(mockSubscriber, response.getBody());
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
