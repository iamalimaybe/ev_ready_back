package com.ev.ready.feedback.dto;

import com.ev.ready.feedback.enums.ChargerFeedbackStatus;

public record ChargerFeedbackSubmissionResponse(
        Long id,
        ChargerFeedbackStatus feedbackStatus,
        String message
) {
}
