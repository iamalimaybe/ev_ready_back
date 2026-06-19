package com.ev.ready.ai.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "evready.ai.recommendation-rate-limit")
public record AiRecommendationRateLimitProperties(
        int maxRequests,
        Duration window,
        Duration entryTtl
) {

    public AiRecommendationRateLimitProperties {
        if (maxRequests <= 0) {
            maxRequests = 2;
        }

        if (window == null || window.isZero() || window.isNegative()) {
            window = Duration.ofMinutes(10);
        }

        if (entryTtl == null || entryTtl.isZero() || entryTtl.isNegative()) {
            entryTtl = Duration.ofMinutes(30);
        }
    }
}