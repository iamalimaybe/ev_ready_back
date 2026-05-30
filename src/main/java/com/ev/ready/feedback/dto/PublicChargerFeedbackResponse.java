package com.ev.ready.feedback.dto;

import com.ev.ready.feedback.domain.ChargerFeedback;
import com.ev.ready.feedback.enums.ChargerFeedbackType;
import java.time.OffsetDateTime;

public record PublicChargerFeedbackResponse(
        Long id,
        Integer rating,
        ChargerFeedbackType feedbackType,
        String message,
        String displayName,
        String city,
        OffsetDateTime createdAt
) {

    public static PublicChargerFeedbackResponse from(ChargerFeedback chargerFeedback) {
        return new PublicChargerFeedbackResponse(
                chargerFeedback.getId(),
                chargerFeedback.getRating(),
                chargerFeedback.getFeedbackType(),
                chargerFeedback.getMessage(),
                chargerFeedback.getDisplayName(),
                chargerFeedback.getCity(),
                chargerFeedback.getCreatedAt()
        );
    }
}
