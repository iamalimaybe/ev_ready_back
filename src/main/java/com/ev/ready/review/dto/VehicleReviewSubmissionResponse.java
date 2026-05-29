package com.ev.ready.review.dto;

import com.ev.ready.review.enums.VehicleReviewStatus;

public record VehicleReviewSubmissionResponse(
        Long id,
        VehicleReviewStatus reviewStatus,
        String message
) {
}
