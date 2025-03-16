package com.flashdash.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FlashDashNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlashDashNotificationApplication.class, args);
    }

}
