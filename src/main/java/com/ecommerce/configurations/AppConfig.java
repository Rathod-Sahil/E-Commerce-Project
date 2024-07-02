package com.ecommerce.configurations;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@EnableScheduling
@Configuration
public class AppConfig {

    private final String HOST;
    private final String USERNAME;
    private final String PASSWORD;

    public AppConfig(@Value("${spring.rabbitmq.host}") String HOST,
            @Value("${spring.rabbitmq.username}") String USERNAME,
            @Value("${spring.rabbitmq.password}") String PASSWORD) {
        this.HOST = HOST;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        return factory.newConnection();
    }

}
