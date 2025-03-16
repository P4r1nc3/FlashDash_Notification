package com.flashdash.notification.repository;

import com.flashdash.notification.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<Subscriber, String> {
    boolean existsByUserFrn(String userFrn);
    void deleteByUserFrn(String userFrn);
}
