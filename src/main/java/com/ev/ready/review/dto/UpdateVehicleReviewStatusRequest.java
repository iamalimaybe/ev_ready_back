package com.ev.ready.review.dto;

import com.ev.ready.review.enums.VehicleReviewStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateVehicleReviewStatusRequest(
        @NotNull(message = "Review status is required.")
        VehicleReviewStatus reviewStatus,

        @Size(max = 500, message = "Moderation reason must be 500 characters or fewer.")
        String moderationReason
) {
}
