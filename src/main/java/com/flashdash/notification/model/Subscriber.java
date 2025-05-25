package com.flashdash.notification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
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

    public String getUserFrn() {
        return userFrn;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getNotificationTime() {
        return notificationTime;
    }

    public boolean isDailyNotifications() {
        return dailyNotifications;
    }

    public NotificationChannel getNotificationChannel() {
        return notificationChannel;
    }

    public void setUserFrn(String userFrn) {
        this.userFrn = userFrn;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setNotificationTime(LocalDateTime notificationTime) {
        this.notificationTime = notificationTime;
    }

    public void setDailyNotifications(boolean dailyNotifications) {
        this.dailyNotifications = dailyNotifications;
    }

    public void setNotificationChannel(NotificationChannel notificationChannel) {
        this.notificationChannel = notificationChannel;
    }
}
