package ru.otus.hw16.message_system.server.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public Gson serializer() {
        return new Gson();
    }
}
