package com.ev.ready.contact.dto;

import com.ev.ready.contact.enums.ContactSubmissionStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateContactSubmissionStatusRequest(
        @NotNull(message = "Contact status is required.")
        ContactSubmissionStatus contactStatus
) {
}
