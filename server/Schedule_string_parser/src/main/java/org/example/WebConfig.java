package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow CORS requests from React running on localhost:5173
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // Allow React frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow methods as needed
                .allowedHeaders("*") // Allow any header
                .allowCredentials(true); // Allow credentials (cookies, etc.)
    }
}
