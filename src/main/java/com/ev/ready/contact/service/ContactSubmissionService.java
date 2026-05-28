package com.ev.ready.contact.service;

import com.ev.ready.common.util.StringUtil;
import com.ev.ready.common.PageResponse;
import com.ev.ready.contact.domain.ContactSubmission;
import com.ev.ready.contact.dto.AdminContactSubmissionResponse;
import com.ev.ready.contact.dto.ContactSubmissionResponse;
import com.ev.ready.contact.dto.CreateContactSubmissionRequest;
import com.ev.ready.contact.repository.ContactSubmissionRepository;
import com.ev.ready.notification.SubmissionNotificationService;
import java.time.OffsetDateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContactSubmissionService {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final ContactSubmissionRepository contactSubmissionRepository;
    private final SubmissionNotificationService submissionNotificationService;

    public ContactSubmissionService(
            ContactSubmissionRepository contactSubmissionRepository,
            SubmissionNotificationService submissionNotificationService
    ) {
        this.contactSubmissionRepository = contactSubmissionRepository;
        this.submissionNotificationService = submissionNotificationService;
    }

    @Transactional
    public ContactSubmissionResponse createContactSubmission(CreateContactSubmissionRequest request) {
        ContactSubmission contactSubmission = new ContactSubmission();
        contactSubmission.setName(StringUtil.trim(request.name()));
        contactSubmission.setEmail(StringUtil.trimToNull(request.email()));
        contactSubmission.setPhone(StringUtil.trimToNull(request.phone()));
        contactSubmission.setOrganization(StringUtil.trimToNull(request.organization()));
        contactSubmission.setInquiryType(StringUtil.trim(request.inquiryType()));
        contactSubmission.setMessage(StringUtil.trim(request.message()));
        contactSubmission.setSourcePage(StringUtil.trimToNull(request.sourcePage()));
        contactSubmission.setConsentAccepted(Boolean.TRUE.equals(request.consentAccepted()));
        contactSubmission.setCreatedAt(OffsetDateTime.now());

        ContactSubmission savedContactSubmission = contactSubmissionRepository.save(contactSubmission);
        submissionNotificationService.notifyContactSubmission(savedContactSubmission);

        return new ContactSubmissionResponse(
                savedContactSubmission.getId(),
                "Contact submission received."
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<AdminContactSubmissionResponse> getAdminContactSubmissions(Integer page, Integer size) {
        return PageResponse.from(contactSubmissionRepository.findAll(pageRequest(page, size))
                .map(AdminContactSubmissionResponse::from));
    }

    @Transactional(readOnly = true)
    public AdminContactSubmissionResponse getAdminContactSubmission(Long id) {
        return contactSubmissionRepository.findById(id)
                .map(AdminContactSubmissionResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Contact submission not found."
                ));
    }

    private PageRequest pageRequest(Integer page, Integer size) {
        int safePage = page == null || page < 0 ? 0 : page;
        int safeSize = size == null || size < 1 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
        return PageRequest.of(safePage, safeSize, Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.desc("id")
        ));
    }
}
