package com.ev.ready.lead.controller;

import com.ev.ready.lead.dto.CreateLeadSubmissionRequest;
import com.ev.ready.lead.dto.LeadSubmissionResponse;
import com.ev.ready.lead.service.LeadSubmissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/leads")
public class LeadSubmissionController {

    private final LeadSubmissionService leadSubmissionService;

    public LeadSubmissionController(LeadSubmissionService leadSubmissionService) {
        this.leadSubmissionService = leadSubmissionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeadSubmissionResponse createLeadSubmission(
            @Valid @RequestBody CreateLeadSubmissionRequest request
    ) {
        return leadSubmissionService.createLeadSubmission(request);
    }
}
