package com.ev.ready.ai.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "evready.ai.recommender")
public record AiRecommenderProperties(
        String baseUrl,
        Duration connectTimeout,
        Duration readTimeout
) {
}