package com.roman.gudkov.newsaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа в приложение News Aggregator.
 *
 * <p>Инициализирует и запускает Spring Boot приложение.</p>
 */
@SpringBootApplication
public class NewsAggregatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(NewsAggregatorApplication.class, args);
    }
}
