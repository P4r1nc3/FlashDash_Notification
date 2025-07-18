package com.flashdash.notification.config.handler;

import com.flashdash.notification.exception.ErrorCode;
import com.flashdash.notification.exception.ErrorDetailsProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ErrorDetailsProvider errorDetailsProvider;

    public CustomAccessDeniedHandler(ErrorDetailsProvider errorDetailsProvider) {
        this.errorDetailsProvider = errorDetailsProvider;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        String errorResponseJson = errorDetailsProvider.createErrorResponseJson(ErrorCode.E401001);
        response.getWriter().write(errorResponseJson);
    }
}
