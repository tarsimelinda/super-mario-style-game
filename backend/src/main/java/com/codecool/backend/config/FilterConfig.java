package com.codecool.backend.config;

import com.codecool.backend.security.DevTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
    private DevTokenFilter devTokenFilter;

    @Bean
    public FilterRegistrationBean<DevTokenFilter> registerDevTokenFilter() {
        FilterRegistrationBean<DevTokenFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(devTokenFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
