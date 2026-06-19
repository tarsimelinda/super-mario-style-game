package com.codecool.backend;

import com.codecool.backend.config.CorsProperties;
import com.codecool.backend.config.GameBalanceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({
        CorsProperties.class,
        GameBalanceProperties.class
})
public class BackendApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(BackendApplication.class, args);

        log.info("Mario Backend started on port {}",
                ctx.getEnvironment().getProperty("server.port"));
    }
}

