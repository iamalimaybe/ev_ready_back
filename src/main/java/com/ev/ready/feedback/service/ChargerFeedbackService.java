package com.ev.ready.feedback.service;

import com.ev.ready.charger.dto.ChargerResponse;
import com.ev.ready.charger.service.ChargerReadService;
import com.ev.ready.common.PageResponse;
import com.ev.ready.common.util.StringUtil;
import com.ev.ready.feedback.domain.ChargerFeedback;
import com.ev.ready.feedback.dto.AdminChargerFeedbackResponse;
import com.ev.ready.feedback.dto.ChargerFeedbackStatusOptionResponse;
import com.ev.ready.feedback.dto.ChargerFeedbackSubmissionResponse;
import com.ev.ready.feedback.dto.ChargerFeedbackTypeOptionResponse;
import com.ev.ready.feedback.dto.CreateChargerFeedbackRequest;
import com.ev.ready.feedback.dto.PublicChargerFeedbackResponse;
import com.ev.ready.feedback.dto.UpdateChargerFeedbackStatusRequest;
import com.ev.ready.feedback.enums.ChargerFeedbackStatus;
import com.ev.ready.feedback.enums.ChargerFeedbackType;
import com.ev.ready.feedback.repository.ChargerFeedbackRepository;
import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ChargerFeedbackService {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PUBLIC_FEEDBACK_PAGE_SIZE = 10;
    private static final int MAX_PUBLIC_FEEDBACK_PAGE_SIZE = 50;
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

    @Transactional(readOnly = true)
    public PageResponse<PublicChargerFeedbackResponse> getApprovedChargerFeedback(
            Long chargerId,
            Integer page,
            Integer size
    ) {
        chargerReadService.ensurePublicChargerExists(chargerId);

        return PageResponse.from(chargerFeedbackRepository.findByChargerIdAndFeedbackStatus(
                chargerId,
                ChargerFeedbackStatus.APPROVED,
                publicFeedbackPageRequest(page, size)
        ).map(PublicChargerFeedbackResponse::from));
    }

    @Transactional(readOnly = true)
    public PageResponse<AdminChargerFeedbackResponse> getAdminChargerFeedback(
            Integer page,
            Integer size,
            ChargerFeedbackStatus feedbackStatus,
            Long chargerId
    ) {
        return PageResponse.from(chargerFeedbackRepository.findAll(
                chargerFeedbackSpecification(feedbackStatus, chargerId),
                pageRequest(page, size)
        ).map(chargerFeedback -> AdminChargerFeedbackResponse.from(
                chargerFeedback,
                chargerName(chargerFeedback.getChargerId())
        )));
    }

    @Transactional(readOnly = true)
    public AdminChargerFeedbackResponse getAdminChargerFeedback(Long id) {
        ChargerFeedback chargerFeedback = chargerFeedbackRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Charger feedback not found."
                ));

        return AdminChargerFeedbackResponse.from(chargerFeedback, chargerName(chargerFeedback.getChargerId()));
    }

    public List<ChargerFeedbackStatusOptionResponse> getAdminChargerFeedbackStatusOptions() {
        return Arrays.stream(ChargerFeedbackStatus.values())
                .map(status -> new ChargerFeedbackStatusOptionResponse(status.name(), label(status)))
                .toList();
    }

    @Transactional
    public AdminChargerFeedbackResponse updateAdminChargerFeedbackStatus(
            Long id,
            UpdateChargerFeedbackStatusRequest request,
            String updatedBy
    ) {
        ChargerFeedback chargerFeedback = chargerFeedbackRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Charger feedback not found."
                ));

        OffsetDateTime now = OffsetDateTime.now();
        String safeUpdatedBy = StringUtil.trimToNull(updatedBy);
        ChargerFeedbackStatus newStatus = request.feedbackStatus();
        boolean movingOutOfPending = chargerFeedback.getFeedbackStatus() == ChargerFeedbackStatus.PENDING
                && newStatus != ChargerFeedbackStatus.PENDING;

        chargerFeedback.setFeedbackStatus(newStatus);
        if (movingOutOfPending) {
            chargerFeedback.setReviewedAt(now);
            chargerFeedback.setReviewedBy(safeUpdatedBy);
        }
        chargerFeedback.setUpdatedAt(now);
        chargerFeedback.setUpdatedBy(safeUpdatedBy);

        return AdminChargerFeedbackResponse.from(chargerFeedback, chargerName(chargerFeedback.getChargerId()));
    }

    private Specification<ChargerFeedback> chargerFeedbackSpecification(
            ChargerFeedbackStatus feedbackStatus,
            Long chargerId
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (feedbackStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("feedbackStatus"), feedbackStatus));
            }
            if (chargerId != null) {
                predicates.add(criteriaBuilder.equal(root.get("chargerId"), chargerId));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private PageRequest pageRequest(Integer page, Integer size) {
        int safePage = page == null || page < 0 ? 0 : page;
        int safeSize = size == null || size < 1 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
        return PageRequest.of(safePage, safeSize, Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.desc("id")
        ));
    }

    private PageRequest publicFeedbackPageRequest(Integer page, Integer size) {
        int safePage = page == null || page < 0 ? 0 : page;
        int safeSize = size == null || size < 1
                ? DEFAULT_PUBLIC_FEEDBACK_PAGE_SIZE
                : Math.min(size, MAX_PUBLIC_FEEDBACK_PAGE_SIZE);
        return PageRequest.of(safePage, safeSize, Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.desc("id")
        ));
    }

    private String chargerName(Long chargerId) {
        try {
            ChargerResponse charger = chargerReadService.getCharger(chargerId);
            return charger.name();
        } catch (ResponseStatusException ex) {
            return null;
        }
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

    private String label(ChargerFeedbackStatus status) {
        return switch (status) {
            case PENDING -> "Pending";
            case APPROVED -> "Approved";
            case REJECTED -> "Rejected";
            case SPAM -> "Spam";
            case APPLIED -> "Applied";
        };
    }
}
