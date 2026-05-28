package com.ev.ready.admin.lead.controller;

import com.ev.ready.common.PageResponse;
import com.ev.ready.lead.dto.AdminLeadSubmissionResponse;
import com.ev.ready.lead.service.LeadSubmissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/leads")
public class AdminLeadSubmissionController {

    private final LeadSubmissionService leadSubmissionService;

    public AdminLeadSubmissionController(LeadSubmissionService leadSubmissionService) {
        this.leadSubmissionService = leadSubmissionService;
    }

    @GetMapping
    public PageResponse<AdminLeadSubmissionResponse> getLeadSubmissions(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return leadSubmissionService.getAdminLeadSubmissions(page, size);
    }

    @GetMapping("/{id}")
    public AdminLeadSubmissionResponse getLeadSubmission(@PathVariable Long id) {
        return leadSubmissionService.getAdminLeadSubmission(id);
    }
}
