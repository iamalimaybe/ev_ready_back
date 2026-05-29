package com.ev.ready.review.dto;

import com.ev.ready.review.enums.VehicleReviewExperienceType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateVehicleReviewRequest(
        @NotNull(message = "Rating is required.")
        @Min(value = 1, message = "Rating must be at least 1.")
        @Max(value = 5, message = "Rating must be at most 5.")
        Integer rating,

        @Size(max = 2000, message = "Review text must be 2000 characters or fewer.")
        String reviewText,

        @Size(max = 120, message = "Display name must be 120 characters or fewer.")
        String displayName,

        @Size(max = 100, message = "City must be 100 characters or fewer.")
        String city,

        @NotNull(message = "Experience type is required.")
        VehicleReviewExperienceType experienceType
) {
}
