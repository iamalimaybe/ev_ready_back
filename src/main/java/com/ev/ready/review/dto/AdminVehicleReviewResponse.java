package com.ev.ready.review.dto;

import com.ev.ready.review.domain.VehicleReview;
import com.ev.ready.review.enums.VehicleReviewExperienceType;
import com.ev.ready.review.enums.VehicleReviewStatus;
import java.time.OffsetDateTime;

public record AdminVehicleReviewResponse(
        Long id,
        Long vehicleId,
        Integer rating,
        String reviewText,
        String displayName,
        String city,
        VehicleReviewExperienceType experienceType,
        VehicleReviewStatus reviewStatus,
        OffsetDateTime moderatedAt,
        String moderatedBy,
        String moderationReason,
        OffsetDateTime createdAt,
        String createdBy,
        OffsetDateTime updatedAt,
        String updatedBy
) {

    public static AdminVehicleReviewResponse from(VehicleReview vehicleReview) {
        return new AdminVehicleReviewResponse(
                vehicleReview.getId(),
                vehicleReview.getVehicleId(),
                vehicleReview.getRating(),
                vehicleReview.getReviewText(),
                vehicleReview.getDisplayName(),
                vehicleReview.getCity(),
                vehicleReview.getExperienceType(),
                vehicleReview.getReviewStatus(),
                vehicleReview.getModeratedAt(),
                vehicleReview.getModeratedBy(),
                vehicleReview.getModerationReason(),
                vehicleReview.getCreatedAt(),
                vehicleReview.getCreatedBy(),
                vehicleReview.getUpdatedAt(),
                vehicleReview.getUpdatedBy()
        );
    }
}
