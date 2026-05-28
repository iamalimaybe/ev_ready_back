package com.ev.ready.lead.service;

import com.ev.ready.common.util.StringUtil;
import com.ev.ready.common.PageResponse;
import com.ev.ready.lead.domain.LeadSubmission;
import com.ev.ready.lead.dto.AdminLeadSubmissionResponse;
import com.ev.ready.lead.dto.CreateLeadSubmissionRequest;
import com.ev.ready.lead.dto.LeadStatusOptionResponse;
import com.ev.ready.lead.dto.LeadSubmissionResponse;
import com.ev.ready.lead.dto.UpdateLeadStatusRequest;
import com.ev.ready.lead.enums.LeadStatus;
import com.ev.ready.lead.repository.LeadSubmissionRepository;
import com.ev.ready.notification.SubmissionNotificationService;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LeadSubmissionService {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

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

    @Transactional(readOnly = true)
    public PageResponse<AdminLeadSubmissionResponse> getAdminLeadSubmissions(Integer page, Integer size) {
        return PageResponse.from(leadSubmissionRepository.findAll(pageRequest(page, size))
                .map(AdminLeadSubmissionResponse::from));
    }

    @Transactional(readOnly = true)
    public AdminLeadSubmissionResponse getAdminLeadSubmission(Long id) {
        return leadSubmissionRepository.findById(id)
                .map(AdminLeadSubmissionResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead submission not found."));
    }

    public List<LeadStatusOptionResponse> getAdminLeadStatusOptions() {
        return Arrays.stream(LeadStatus.values())
                .map(status -> new LeadStatusOptionResponse(status.name(), label(status)))
                .toList();
    }

    @Transactional
    public AdminLeadSubmissionResponse updateAdminLeadStatus(
            Long id,
            UpdateLeadStatusRequest request,
            String updatedBy
    ) {
        LeadSubmission leadSubmission = leadSubmissionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead submission not found."));

        leadSubmission.setLeadStatus(request.leadStatus());
        leadSubmission.setUpdatedBy(updatedBy);
        leadSubmission.setUpdatedAt(OffsetDateTime.now());

        return AdminLeadSubmissionResponse.from(leadSubmission);
    }

    private PageRequest pageRequest(Integer page, Integer size) {
        int safePage = page == null || page < 0 ? 0 : page;
        int safeSize = size == null || size < 1 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
        return PageRequest.of(safePage, safeSize, Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.desc("id")
        ));
    }

    private String label(LeadStatus status) {
        String[] words = status.name().toLowerCase(Locale.ROOT).split("_");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase(Locale.ROOT) + words[i].substring(1);
        }
        return String.join(" ", words);
    }
}
