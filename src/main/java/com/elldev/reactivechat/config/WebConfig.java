package com.elldev.reactivechat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${web.allowed-origins}")
    private String[] origins;

    @Value("${web.ssl}")
    private boolean ssl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowCredentials(true);
    }

    @Bean
    public boolean isHttpSecure() {
        return this.ssl;
    }
}
