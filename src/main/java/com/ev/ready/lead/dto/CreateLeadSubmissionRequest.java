package com.ev.ready.lead.dto;

import com.ev.ready.lead.enums.LeadInterestType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateLeadSubmissionRequest(
        @NotBlank(message = "Name is required.")
        @Size(max = 120, message = "Name must be 120 characters or fewer.")
        String name,

        @NotBlank(message = "Phone is required.")
        @Size(max = 40, message = "Phone must be 40 characters or fewer.")
        String phone,

        @Size(max = 100, message = "City must be 100 characters or fewer.")
        String city,

        @NotNull(message = "Interest type is required.")
        LeadInterestType interestType,

        @Size(max = 2000, message = "Message must be 2000 characters or fewer.")
        String message,

        @Size(max = 120, message = "Source page must be 120 characters or fewer.")
        String sourcePage,

        @AssertTrue(message = "Consent must be accepted.")
        Boolean consentAccepted
) {
}
