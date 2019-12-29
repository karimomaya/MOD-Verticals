package com.mod.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;

/**
 * Created by karim.omaya on 12/10/2019.
 */
@EnableJpaRepositories("com.mod.rest.repository")
@EnableJpaAuditing
@EntityScan("com.mod.rest.model")
@ComponentScan("com.mod.rest")
@SpringBootApplication
public class SpringBootRestServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringBootRestServiceApplication.class);
        app.setDefaultProperties(Collections
                .singletonMap("server.port", "8081"));
        app.run(args);
    }
}
