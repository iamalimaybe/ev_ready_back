package com.ev.ready.contact.dto;

import com.ev.ready.contact.domain.ContactSubmission;
import com.ev.ready.contact.enums.ContactSubmissionStatus;
import java.time.OffsetDateTime;

public record AdminContactSubmissionResponse(
        Long id,
        String name,
        String email,
        String phone,
        String organization,
        String inquiryType,
        String message,
        String sourcePage,
        Boolean consentAccepted,
        ContactSubmissionStatus contactStatus,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static AdminContactSubmissionResponse from(ContactSubmission contactSubmission) {
        return new AdminContactSubmissionResponse(
                contactSubmission.getId(),
                contactSubmission.getName(),
                contactSubmission.getEmail(),
                contactSubmission.getPhone(),
                contactSubmission.getOrganization(),
                contactSubmission.getInquiryType(),
                contactSubmission.getMessage(),
                contactSubmission.getSourcePage(),
                contactSubmission.getConsentAccepted(),
                contactSubmission.getContactStatus(),
                contactSubmission.getCreatedAt(),
                contactSubmission.getUpdatedAt()
        );
    }
}
