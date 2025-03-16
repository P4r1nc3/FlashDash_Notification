package com.flashdash.notification.repository;

import com.flashdash.notification.model.NotificationSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationSubscriberRepository extends JpaRepository<NotificationSubscriber, String> {
    boolean existsByUserFrn(String userFrn);
}
