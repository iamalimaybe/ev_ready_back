package com.ev.ready.lead.dto;

import com.ev.ready.lead.enums.LeadStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateLeadStatusRequest(
        @NotNull(message = "Lead status is required.")
        LeadStatus leadStatus
) {
}
