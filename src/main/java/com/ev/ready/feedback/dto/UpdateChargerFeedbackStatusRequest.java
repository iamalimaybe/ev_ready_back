package com.ev.ready.feedback.dto;

import com.ev.ready.feedback.enums.ChargerFeedbackStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateChargerFeedbackStatusRequest(
        @NotNull(message = "Feedback status is required.")
        ChargerFeedbackStatus feedbackStatus
) {
}
