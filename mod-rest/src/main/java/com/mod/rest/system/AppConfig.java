package com.mod.rest.system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by karim.omaya on 12/24/2019.
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public UserInterceptor userCustomInterceptor() {
        return new UserInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userCustomInterceptor());
    }
}
