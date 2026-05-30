package com.ev.ready.admin.feedback.controller;

import com.ev.ready.common.PageResponse;
import com.ev.ready.feedback.dto.AdminChargerFeedbackResponse;
import com.ev.ready.feedback.dto.ChargerFeedbackStatusOptionResponse;
import com.ev.ready.feedback.dto.UpdateChargerFeedbackStatusRequest;
import com.ev.ready.feedback.enums.ChargerFeedbackStatus;
import com.ev.ready.feedback.service.ChargerFeedbackService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/charger-feedback")
public class AdminChargerFeedbackController {

    private final ChargerFeedbackService chargerFeedbackService;

    public AdminChargerFeedbackController(ChargerFeedbackService chargerFeedbackService) {
        this.chargerFeedbackService = chargerFeedbackService;
    }

    @GetMapping
    public PageResponse<AdminChargerFeedbackResponse> getChargerFeedback(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) ChargerFeedbackStatus feedbackStatus,
            @RequestParam(required = false) Long chargerId
    ) {
        return chargerFeedbackService.getAdminChargerFeedback(page, size, feedbackStatus, chargerId);
    }

    @GetMapping("/statuses")
    public List<ChargerFeedbackStatusOptionResponse> getChargerFeedbackStatusOptions() {
        return chargerFeedbackService.getAdminChargerFeedbackStatusOptions();
    }

    @GetMapping("/{id}")
    public AdminChargerFeedbackResponse getChargerFeedback(@PathVariable Long id) {
        return chargerFeedbackService.getAdminChargerFeedback(id);
    }

    @PatchMapping("/{id}/status")
    public AdminChargerFeedbackResponse updateChargerFeedbackStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChargerFeedbackStatusRequest request,
            Authentication authentication
    ) {
        return chargerFeedbackService.updateAdminChargerFeedbackStatus(id, request, authentication.getName());
    }
}
