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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubscriberServiceTest {

    @Autowired
    private SubscriberService subscriberService;

    @MockitoBean
    private UserContext userContext;

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
        subscriberService.registerUser();

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
                () -> subscriberService.registerUser()
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
        subscriberService.unregisterUser();

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
                () -> subscriberService.unregisterUser()
        );

        assertEquals(ErrorCode.E404001, exception.getErrorCode());
        assertEquals("User not found.", exception.getMessage());
        verify(repository, never()).deleteByUserFrn(anyString());
    }
}
