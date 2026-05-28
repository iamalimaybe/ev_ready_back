package com.ev.ready.lead.dto;

import com.ev.ready.lead.domain.LeadSubmission;
import com.ev.ready.lead.enums.LeadInterestType;
import com.ev.ready.lead.enums.LeadStatus;
import java.time.OffsetDateTime;

public record AdminLeadSubmissionResponse(
        Long id,
        String name,
        String phone,
        String city,
        LeadInterestType interestType,
        String message,
        String sourcePage,
        Boolean consentAccepted,
        LeadStatus leadStatus,
        Boolean active,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {

    public static AdminLeadSubmissionResponse from(LeadSubmission leadSubmission) {
        return new AdminLeadSubmissionResponse(
                leadSubmission.getId(),
                leadSubmission.getName(),
                leadSubmission.getPhone(),
                leadSubmission.getCity(),
                leadSubmission.getInterestType(),
                leadSubmission.getMessage(),
                leadSubmission.getSourcePage(),
                leadSubmission.getConsentAccepted(),
                leadSubmission.getLeadStatus(),
                leadSubmission.getActive(),
                leadSubmission.getCreatedAt(),
                leadSubmission.getUpdatedAt()
        );
    }
}
