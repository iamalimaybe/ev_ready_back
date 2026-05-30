package com.ev.ready.feedback.controller;

import com.ev.ready.feedback.dto.ChargerFeedbackSubmissionResponse;
import com.ev.ready.feedback.dto.ChargerFeedbackTypeOptionResponse;
import com.ev.ready.feedback.dto.CreateChargerFeedbackRequest;
import com.ev.ready.feedback.service.ChargerFeedbackService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chargers")
public class ChargerFeedbackController {

    private final ChargerFeedbackService chargerFeedbackService;

    public ChargerFeedbackController(ChargerFeedbackService chargerFeedbackService) {
        this.chargerFeedbackService = chargerFeedbackService;
    }

    @GetMapping("/feedback-types")
    public List<ChargerFeedbackTypeOptionResponse> getFeedbackTypeOptions() {
        return chargerFeedbackService.getFeedbackTypeOptions();
    }

    @PostMapping("/{chargerId}/feedback")
    @ResponseStatus(HttpStatus.CREATED)
    public ChargerFeedbackSubmissionResponse createChargerFeedback(
            @PathVariable Long chargerId,
            @Valid @RequestBody CreateChargerFeedbackRequest request
    ) {
        return chargerFeedbackService.createChargerFeedback(chargerId, request);
    }
}
