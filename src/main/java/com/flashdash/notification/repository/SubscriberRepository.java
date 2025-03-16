package com.flashdash.notification.repository;

import com.flashdash.notification.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriberRepository extends JpaRepository<Subscriber, String> {
    boolean existsByUserFrn(String userFrn);
    Optional<Subscriber> findByUserFrn(String userFrn);
    List<Subscriber> findByDailyNotificationsTrue();
    void deleteByUserFrn(String userFrn);
}
