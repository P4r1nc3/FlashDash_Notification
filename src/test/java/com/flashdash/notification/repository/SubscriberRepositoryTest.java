package com.flashdash.notification.repository;

import com.flashdash.notification.model.NotificationChannel;
import com.flashdash.notification.model.Subscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubscriberRepositoryTest {

    @Autowired
    private SubscriberRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private Subscriber createSubscriber(String userFrn, String email) {
        Subscriber subscriber = new Subscriber();
        subscriber.setUserFrn(userFrn);
        subscriber.setEmail(email);
        subscriber.setCreatedAt(LocalDateTime.now());
        subscriber.setUpdatedAt(LocalDateTime.now());
        subscriber.setNotificationTime(LocalDateTime.now());
        subscriber.setDailyNotifications(true);
        subscriber.setNotificationChannel(NotificationChannel.EMAIL);
        return subscriber;
    }

    @Test
    void shouldFindSubscriberByUserFrn() {
        // Arrange
        Subscriber subscriber = createSubscriber("user-123", "test@example.com");
        repository.save(subscriber);

        // Act
        Optional<Subscriber> foundSubscriber = repository.findByUserFrn("user-123");

        // Assert
        assertThat(foundSubscriber).isPresent();
        assertThat(foundSubscriber.get().getUserFrn()).isEqualTo("user-123");
        assertThat(foundSubscriber.get().getEmail()).isEqualTo("test@example.com");
        assertThat(foundSubscriber.get().isDailyNotifications()).isTrue();
    }

    @Test
    void shouldReturnEmptyWhenUserFrnDoesNotExist() {
        // Act
        Optional<Subscriber> foundSubscriber = repository.findByUserFrn("nonexistent-userFrn");

        // Assert
        assertThat(foundSubscriber).isNotPresent();
    }

    @Test
    void shouldCheckIfUserFrnExists() {
        // Arrange
        Subscriber subscriber = createSubscriber("user-456", "test2@example.com");
        repository.save(subscriber);

        // Act
        boolean exists = repository.existsByUserFrn("user-456");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenUserFrnDoesNotExist() {
        // Act
        boolean exists = repository.existsByUserFrn("nonexistent-userFrn");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    void shouldDeleteSubscriberByUserFrn() {
        // Arrange
        Subscriber subscriber = createSubscriber("user-789", "test3@example.com");
        repository.save(subscriber);
        assertThat(repository.existsByUserFrn("user-789")).isTrue();

        // Act
        repository.deleteByUserFrn("user-789");

        // Assert
        assertThat(repository.existsByUserFrn("user-789")).isFalse();
    }
}
