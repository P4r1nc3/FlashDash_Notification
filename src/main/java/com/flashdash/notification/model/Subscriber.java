package com.flashdash.notification.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "subscribers")
public class Subscriber {

    @Id
    @Column(name = "user_frn", nullable = false, length = 256)
    private String userFrn;

    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "notification_time", nullable = false)
    private LocalDateTime notificationTime;

    @Column(name = "daily_notifications", nullable = false)
    private boolean dailyNotifications;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_channel", nullable = false)
    private NotificationChannel notificationChannel;

    public Subscriber() {}
}
