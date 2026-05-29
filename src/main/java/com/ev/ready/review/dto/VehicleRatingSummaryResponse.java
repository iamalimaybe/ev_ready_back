package com.ev.ready.review.dto;

import java.math.BigDecimal;

public record VehicleRatingSummaryResponse(
        BigDecimal averageRating,
        Long ratingCount
) {

    public static VehicleRatingSummaryResponse empty() {
        return new VehicleRatingSummaryResponse(null, 0L);
    }
}
