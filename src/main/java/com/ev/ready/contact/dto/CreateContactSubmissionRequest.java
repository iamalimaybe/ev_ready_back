package com.ev.ready.contact.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateContactSubmissionRequest(
        @NotBlank(message = "Name is required.")
        @Size(max = 120, message = "Name must be 120 characters or fewer.")
        String name,

        @Size(max = 160, message = "Email must be 160 characters or fewer.")
        String email,

        @Size(max = 40, message = "Phone must be 40 characters or fewer.")
        String phone,

        @Size(max = 160, message = "Organization must be 160 characters or fewer.")
        String organization,

        @NotBlank(message = "Inquiry type is required.")
        @Size(max = 80, message = "Inquiry type must be 80 characters or fewer.")
        String inquiryType,

        @NotBlank(message = "Message is required.")
        @Size(max = 2000, message = "Message must be 2000 characters or fewer.")
        String message,

        @Size(max = 120, message = "Source page must be 120 characters or fewer.")
        String sourcePage,

        @NotNull(message = "Consent must be accepted.")
        @AssertTrue(message = "Consent must be accepted.")
        Boolean consentAccepted
) {
}
