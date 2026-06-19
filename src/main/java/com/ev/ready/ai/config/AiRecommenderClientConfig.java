package com.ev.ready.ai.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties({
        AiRecommenderProperties.class,
        AiRecommendationRateLimitProperties.class
})
public class AiRecommenderClientConfig {

    @Bean
    public RestClient aiRecommenderRestClient(AiRecommenderProperties properties) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.connectTimeout());
        requestFactory.setReadTimeout(properties.readTimeout());

        return RestClient.builder()
                .baseUrl(normalizedBaseUrl(properties.baseUrl()))
                .requestFactory(requestFactory)
                .build();
    }

    private String normalizedBaseUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("AI recommender base URL is not configured.");
        }

        String trimmedBaseUrl = baseUrl.trim();

        if (trimmedBaseUrl.endsWith("/")) {
            return trimmedBaseUrl.substring(0, trimmedBaseUrl.length() - 1);
        }

        return trimmedBaseUrl;
    }
}