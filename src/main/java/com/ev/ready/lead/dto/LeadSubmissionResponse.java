package com.ev.ready.lead.dto;

import com.ev.ready.lead.enums.LeadStatus;

public record LeadSubmissionResponse(
        Long id,
        LeadStatus leadStatus,
        String message
) {
}
