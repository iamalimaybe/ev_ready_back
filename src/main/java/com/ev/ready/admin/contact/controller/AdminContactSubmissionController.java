package com.ev.ready.admin.contact.controller;

import com.ev.ready.common.PageResponse;
import com.ev.ready.contact.dto.AdminContactSubmissionResponse;
import com.ev.ready.contact.dto.ContactSubmissionStatusOptionResponse;
import com.ev.ready.contact.dto.UpdateContactSubmissionStatusRequest;
import com.ev.ready.contact.service.ContactSubmissionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/contact-submissions")
public class AdminContactSubmissionController {

    private final ContactSubmissionService contactSubmissionService;

    public AdminContactSubmissionController(ContactSubmissionService contactSubmissionService) {
        this.contactSubmissionService = contactSubmissionService;
    }

    @GetMapping
    public PageResponse<AdminContactSubmissionResponse> getContactSubmissions(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return contactSubmissionService.getAdminContactSubmissions(page, size);
    }

    @GetMapping("/statuses")
    public List<ContactSubmissionStatusOptionResponse> getContactStatusOptions() {
        return contactSubmissionService.getAdminContactStatusOptions();
    }

    @GetMapping("/{id}")
    public AdminContactSubmissionResponse getContactSubmission(@PathVariable Long id) {
        return contactSubmissionService.getAdminContactSubmission(id);
    }

    @PatchMapping("/{id}/status")
    public AdminContactSubmissionResponse updateContactStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateContactSubmissionStatusRequest request,
            Authentication authentication
    ) {
        return contactSubmissionService.updateAdminContactStatus(id, request, authentication.getName());
    }
}
