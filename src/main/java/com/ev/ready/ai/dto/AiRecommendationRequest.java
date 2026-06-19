package com.ev.ready.ai.dto;

public record AiRecommendationRequest(
        String vehicleType,
        Long budgetPkr,
        String city,
        Integer dailyDistanceKm,
        Integer monthlyDistanceKm,
        Boolean homeChargingAvailable,
        Boolean solarAvailable,
        String primaryUseCase,
        Integer familySize,
        String priority,
        String additionalNotes
) {
}