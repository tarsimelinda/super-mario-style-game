package com.codecool.backend.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DevTokenFilter implements Filter {

    private final String devToken;

    public DevTokenFilter(@Value("${app.dev.token}") String devToken) {
        this.devToken = devToken;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) request;
        String token = http.getHeader("X-API-KEY");

        if (token == null || !token.equals(devToken)) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }
}
