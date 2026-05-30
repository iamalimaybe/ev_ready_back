package com.ev.ready.feedback.dto;

import com.ev.ready.feedback.enums.ChargerFeedbackType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateChargerFeedbackRequest(
        @Min(value = 1, message = "Rating must be at least 1.")
        @Max(value = 5, message = "Rating must be at most 5.")
        Integer rating,

        @NotNull(message = "Feedback type is required.")
        ChargerFeedbackType feedbackType,

        @Size(max = 2000, message = "Message must be 2000 characters or fewer.")
        String message,

        @Size(max = 120, message = "Display name must be 120 characters or fewer.")
        String displayName,

        @Size(max = 100, message = "City must be 100 characters or fewer.")
        String city,

        @Size(max = 160, message = "Contact must be 160 characters or fewer.")
        String reportedByContact
) {
}
