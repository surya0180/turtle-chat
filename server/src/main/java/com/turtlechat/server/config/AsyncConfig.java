package com.turtlechat.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean("cachedThreadPool")
    public Executor cachedThreadPool() {
        return Executors.newCachedThreadPool();
    }
}
