package com.ev.ready.review.dto;

import com.ev.ready.review.domain.VehicleReview;
import com.ev.ready.review.enums.VehicleReviewExperienceType;
import java.time.OffsetDateTime;

public record PublicVehicleReviewResponse(
        Long id,
        Integer rating,
        String reviewText,
        String displayName,
        String city,
        VehicleReviewExperienceType experienceType,
        OffsetDateTime createdAt
) {

    public static PublicVehicleReviewResponse from(VehicleReview vehicleReview) {
        return new PublicVehicleReviewResponse(
                vehicleReview.getId(),
                vehicleReview.getRating(),
                vehicleReview.getReviewText(),
                vehicleReview.getDisplayName(),
                vehicleReview.getCity(),
                vehicleReview.getExperienceType(),
                vehicleReview.getCreatedAt()
        );
    }
}
