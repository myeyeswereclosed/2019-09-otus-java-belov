package ru.otus.hw16.db_service.config;

import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public GsonBuilder serializer() {
        return new GsonBuilder();
    }
}
