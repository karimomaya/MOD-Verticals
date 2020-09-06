package com.mod.rest;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    private static ApplicationContext applicationContext = null;

    public static void main(String[] args) {
        String mode = args != null && args.length > 0 ? args[0] : null;

        if (applicationContext != null && mode != null && "stop".equals(mode)) {
            System.exit(SpringApplication.exit(applicationContext, new ExitCodeGenerator() {
                @Override
                public int getExitCode() {
                    return 0;
                }
            }));
        }
        else {
            SpringApplication app = new SpringApplication(SpringBootRestServiceApplication.class);
            app.setDefaultProperties(Collections
                    .singletonMap("server.port", "8081"));
            applicationContext = app.run(args);
        }
    }

    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/*").allowedOrigins("*").allowedHeaders("*").allowedMethods("*");
            }
        };
    }
}
