package com.ev.ready.contact.service;

import com.ev.ready.common.util.StringUtil;
import com.ev.ready.contact.domain.ContactSubmission;
import com.ev.ready.contact.dto.ContactSubmissionResponse;
import com.ev.ready.contact.dto.CreateContactSubmissionRequest;
import com.ev.ready.contact.repository.ContactSubmissionRepository;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactSubmissionService {

    private final ContactSubmissionRepository contactSubmissionRepository;

    public ContactSubmissionService(ContactSubmissionRepository contactSubmissionRepository) {
        this.contactSubmissionRepository = contactSubmissionRepository;
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

        return new ContactSubmissionResponse(
                savedContactSubmission.getId(),
                "Contact submission received."
        );
    }
}
