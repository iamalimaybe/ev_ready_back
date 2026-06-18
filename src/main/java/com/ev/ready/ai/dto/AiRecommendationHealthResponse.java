package com.ev.ready.ai.dto;

public record AiRecommendationHealthResponse(
        String status
) {

    public static AiRecommendationHealthResponse up() {
        return new AiRecommendationHealthResponse("UP");
    }

    public static AiRecommendationHealthResponse down() {
        return new AiRecommendationHealthResponse("DOWN");
    }
}