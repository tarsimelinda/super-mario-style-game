package com.codecool.backend.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiKeyFilter implements Filter {

    private static final String API_KEY_HEADER = "X-API-KEY";

    private final String expectedApiKey;

    public ApiKeyFilter(@Value("${app.api-key}") String expectedApiKey) {
        this.expectedApiKey = expectedApiKey;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(req, res);
            return;
        }

        String actualApiKey = request.getHeader(API_KEY_HEADER);

        if (expectedApiKey.equals(actualApiKey)) {
            chain.doFilter(req, res);
            return;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("""
        {
          "code": "UNAUTHORIZED",
          "details": "Missing or invalid API key."
        }
        """);
    }
}