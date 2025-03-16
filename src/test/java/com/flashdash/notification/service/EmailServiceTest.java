package com.flashdash.notification.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @MockitoBean
    private JavaMailSender mailSender;

    @MockitoBean
    private MimeMessage mimeMessage;

    @Test
    void testSendEmail() {
        // Given
        String to = "test@example.com";
        String subject = "Test Subject";
        String content = "Test Content";

        // When
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailService.sendEmail(to, subject, content);

        // Then
        verify(mailSender, times(1)).send(mimeMessage);
    }
}
