package com.ev.ready.feedback.dto;

import com.ev.ready.feedback.domain.ChargerFeedback;
import com.ev.ready.feedback.enums.ChargerFeedbackStatus;
import com.ev.ready.feedback.enums.ChargerFeedbackType;
import java.time.OffsetDateTime;

public record AdminChargerFeedbackResponse(
        Long id,
        Long chargerId,
        String chargerName,
        Integer rating,
        ChargerFeedbackType feedbackType,
        String message,
        String displayName,
        String city,
        String reportedByContact,
        ChargerFeedbackStatus feedbackStatus,
        OffsetDateTime reviewedAt,
        String reviewedBy,
        OffsetDateTime createdAt,
        String createdBy,
        OffsetDateTime updatedAt,
        String updatedBy
) {

    public static AdminChargerFeedbackResponse from(ChargerFeedback chargerFeedback, String chargerName) {
        return new AdminChargerFeedbackResponse(
                chargerFeedback.getId(),
                chargerFeedback.getChargerId(),
                chargerName,
                chargerFeedback.getRating(),
                chargerFeedback.getFeedbackType(),
                chargerFeedback.getMessage(),
                chargerFeedback.getDisplayName(),
                chargerFeedback.getCity(),
                chargerFeedback.getReportedByContact(),
                chargerFeedback.getFeedbackStatus(),
                chargerFeedback.getReviewedAt(),
                chargerFeedback.getReviewedBy(),
                chargerFeedback.getCreatedAt(),
                chargerFeedback.getCreatedBy(),
                chargerFeedback.getUpdatedAt(),
                chargerFeedback.getUpdatedBy()
        );
    }
}
