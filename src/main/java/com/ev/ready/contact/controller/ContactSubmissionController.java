package com.ev.ready.contact.controller;

import com.ev.ready.contact.dto.ContactSubmissionResponse;
import com.ev.ready.contact.dto.CreateContactSubmissionRequest;
import com.ev.ready.contact.service.ContactSubmissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contact-submissions")
public class ContactSubmissionController {

    private final ContactSubmissionService contactSubmissionService;

    public ContactSubmissionController(ContactSubmissionService contactSubmissionService) {
        this.contactSubmissionService = contactSubmissionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactSubmissionResponse createContactSubmission(
            @Valid @RequestBody CreateContactSubmissionRequest request
    ) {
        return contactSubmissionService.createContactSubmission(request);
    }
}
