package com.ev.ready.lead.service;

import com.ev.ready.common.util.StringUtil;
import com.ev.ready.lead.domain.LeadSubmission;
import com.ev.ready.lead.dto.CreateLeadSubmissionRequest;
import com.ev.ready.lead.dto.LeadSubmissionResponse;
import com.ev.ready.lead.enums.LeadStatus;
import com.ev.ready.lead.repository.LeadSubmissionRepository;
import com.ev.ready.notification.SubmissionNotificationService;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeadSubmissionService {

    private final LeadSubmissionRepository leadSubmissionRepository;
    private final SubmissionNotificationService submissionNotificationService;

    public LeadSubmissionService(
            LeadSubmissionRepository leadSubmissionRepository,
            SubmissionNotificationService submissionNotificationService
    ) {
        this.leadSubmissionRepository = leadSubmissionRepository;
        this.submissionNotificationService = submissionNotificationService;
    }

    @Transactional
    public LeadSubmissionResponse createLeadSubmission(CreateLeadSubmissionRequest request) {
        LeadSubmission leadSubmission = new LeadSubmission();
        leadSubmission.setName(StringUtil.trim(request.name()));
        leadSubmission.setPhone(StringUtil.trim(request.phone()));
        leadSubmission.setCity(StringUtil.trimToNull(request.city()));
        leadSubmission.setInterestType(request.interestType());
        leadSubmission.setMessage(StringUtil.trimToNull(request.message()));
        leadSubmission.setSourcePage(StringUtil.trimToNull(request.sourcePage()));
        leadSubmission.setConsentAccepted(Boolean.TRUE.equals(request.consentAccepted()));
        leadSubmission.setLeadStatus(LeadStatus.NEW);
        leadSubmission.setActive(true);
        leadSubmission.setCreatedAt(OffsetDateTime.now());

        LeadSubmission savedLeadSubmission = leadSubmissionRepository.save(leadSubmission);
        submissionNotificationService.notifyLeadSubmission(savedLeadSubmission);

        return new LeadSubmissionResponse(
                savedLeadSubmission.getId(),
                savedLeadSubmission.getLeadStatus(),
                "Lead submission received."
        );
    }
}
