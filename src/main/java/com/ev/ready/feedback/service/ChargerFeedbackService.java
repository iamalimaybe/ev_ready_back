package com.ev.ready.feedback.service;

import com.ev.ready.charger.service.ChargerReadService;
import com.ev.ready.common.util.StringUtil;
import com.ev.ready.feedback.domain.ChargerFeedback;
import com.ev.ready.feedback.dto.ChargerFeedbackSubmissionResponse;
import com.ev.ready.feedback.dto.ChargerFeedbackTypeOptionResponse;
import com.ev.ready.feedback.dto.CreateChargerFeedbackRequest;
import com.ev.ready.feedback.enums.ChargerFeedbackStatus;
import com.ev.ready.feedback.enums.ChargerFeedbackType;
import com.ev.ready.feedback.repository.ChargerFeedbackRepository;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChargerFeedbackService {

    private static final String PUBLIC_AUDIT_USER = "public";

    private final ChargerFeedbackRepository chargerFeedbackRepository;
    private final ChargerReadService chargerReadService;

    public ChargerFeedbackService(
            ChargerFeedbackRepository chargerFeedbackRepository,
            ChargerReadService chargerReadService
    ) {
        this.chargerFeedbackRepository = chargerFeedbackRepository;
        this.chargerReadService = chargerReadService;
    }

    @Transactional
    public ChargerFeedbackSubmissionResponse createChargerFeedback(
            Long chargerId,
            CreateChargerFeedbackRequest request
    ) {
        chargerReadService.ensurePublicChargerExists(chargerId);

        ChargerFeedback chargerFeedback = new ChargerFeedback();
        chargerFeedback.setChargerId(chargerId);
        chargerFeedback.setRating(request.rating());
        chargerFeedback.setFeedbackType(request.feedbackType());
        chargerFeedback.setMessage(StringUtil.trimToNull(request.message()));
        chargerFeedback.setDisplayName(StringUtil.trimToNull(request.displayName()));
        chargerFeedback.setCity(StringUtil.trimToNull(request.city()));
        chargerFeedback.setReportedByContact(StringUtil.trimToNull(request.reportedByContact()));
        chargerFeedback.setFeedbackStatus(ChargerFeedbackStatus.PENDING);
        chargerFeedback.setCreatedAt(OffsetDateTime.now());
        chargerFeedback.setCreatedBy(PUBLIC_AUDIT_USER);

        ChargerFeedback savedChargerFeedback = chargerFeedbackRepository.save(chargerFeedback);

        return new ChargerFeedbackSubmissionResponse(
                savedChargerFeedback.getId(),
                savedChargerFeedback.getFeedbackStatus(),
                "Feedback submitted for moderation. It will not be published unless approved."
        );
    }

    public List<ChargerFeedbackTypeOptionResponse> getFeedbackTypeOptions() {
        return Arrays.stream(ChargerFeedbackType.values())
                .map(feedbackType -> new ChargerFeedbackTypeOptionResponse(
                        feedbackType.name(),
                        label(feedbackType)
                ))
                .toList();
    }

    private String label(ChargerFeedbackType feedbackType) {
        return switch (feedbackType) {
            case WORKING -> "Working";
            case NOT_WORKING -> "Not working";
            case CONNECTOR_UNAVAILABLE -> "Connector unavailable";
            case PRICE_CHANGED -> "Price changed";
            case ACCESS_ISSUE -> "Access issue";
            case LOCATION_WRONG -> "Location wrong";
            case CLOSED_OR_REMOVED -> "Closed or removed";
            case OTHER -> "Other";
        };
    }
}
