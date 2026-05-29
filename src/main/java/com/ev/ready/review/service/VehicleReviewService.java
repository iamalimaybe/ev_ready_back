package com.ev.ready.review.service;

import com.ev.ready.catalog.service.VehicleReadService;
import com.ev.ready.common.PageResponse;
import com.ev.ready.common.util.StringUtil;
import com.ev.ready.review.domain.VehicleReview;
import com.ev.ready.review.dto.AdminVehicleReviewResponse;
import com.ev.ready.review.dto.CreateVehicleReviewRequest;
import com.ev.ready.review.dto.PublicVehicleReviewResponse;
import com.ev.ready.review.dto.UpdateVehicleReviewStatusRequest;
import com.ev.ready.review.dto.VehicleRatingSummaryResponse;
import com.ev.ready.review.dto.VehicleReviewExperienceTypeOptionResponse;
import com.ev.ready.review.dto.VehicleReviewSubmissionResponse;
import com.ev.ready.review.dto.VehicleReviewStatusOptionResponse;
import com.ev.ready.review.enums.VehicleReviewExperienceType;
import com.ev.ready.review.enums.VehicleReviewStatus;
import com.ev.ready.review.repository.VehicleReviewRepository;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VehicleReviewService {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PUBLIC_REVIEW_PAGE_SIZE = 10;
    private static final int MAX_PUBLIC_REVIEW_PAGE_SIZE = 50;
    private static final String PUBLIC_AUDIT_USER = "public";

    private final VehicleReviewRepository vehicleReviewRepository;
    private final VehicleReadService vehicleReadService;

    public VehicleReviewService(
            VehicleReviewRepository vehicleReviewRepository,
            VehicleReadService vehicleReadService
    ) {
        this.vehicleReviewRepository = vehicleReviewRepository;
        this.vehicleReadService = vehicleReadService;
    }

    @Transactional
    public VehicleReviewSubmissionResponse createVehicleReview(Long vehicleId, CreateVehicleReviewRequest request) {
        vehicleReadService.ensurePublicVehicleExists(vehicleId);

        VehicleReview vehicleReview = new VehicleReview();
        vehicleReview.setVehicleId(vehicleId);
        vehicleReview.setRating(request.rating());
        vehicleReview.setReviewText(StringUtil.trimToNull(request.reviewText()));
        vehicleReview.setDisplayName(StringUtil.trimToNull(request.displayName()));
        vehicleReview.setCity(StringUtil.trimToNull(request.city()));
        vehicleReview.setExperienceType(request.experienceType());
        vehicleReview.setReviewStatus(VehicleReviewStatus.PENDING);
        vehicleReview.setCreatedAt(OffsetDateTime.now());
        vehicleReview.setCreatedBy(PUBLIC_AUDIT_USER);

        VehicleReview savedVehicleReview = vehicleReviewRepository.save(vehicleReview);

        return new VehicleReviewSubmissionResponse(
                savedVehicleReview.getId(),
                savedVehicleReview.getReviewStatus(),
                "Review submitted for moderation. It will not be published unless approved."
        );
    }

    public List<VehicleReviewExperienceTypeOptionResponse> getExperienceTypeOptions() {
        return Arrays.stream(VehicleReviewExperienceType.values())
                .map(experienceType -> new VehicleReviewExperienceTypeOptionResponse(
                        experienceType.name(),
                        label(experienceType)
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public VehicleRatingSummaryResponse getApprovedRatingSummary(Long vehicleId) {
        return getApprovedRatingSummaries(List.of(vehicleId)).getOrDefault(
                vehicleId,
                VehicleRatingSummaryResponse.empty()
        );
    }

    @Transactional(readOnly = true)
    public Map<Long, VehicleRatingSummaryResponse> getApprovedRatingSummaries(Collection<Long> vehicleIds) {
        if (vehicleIds == null || vehicleIds.isEmpty()) {
            return Map.of();
        }

        return vehicleReviewRepository.findRatingSummaries(vehicleIds, VehicleReviewStatus.APPROVED)
                .stream()
                .collect(Collectors.toMap(
                        summary -> summary.getVehicleId(),
                        summary -> new VehicleRatingSummaryResponse(
                                roundAverage(summary.getAverageRating()),
                                summary.getRatingCount()
                        )
                ));
    }

    @Transactional(readOnly = true)
    public PageResponse<PublicVehicleReviewResponse> getApprovedVehicleReviews(
            Long vehicleId,
            Integer page,
            Integer size
    ) {
        vehicleReadService.ensurePublicVehicleExists(vehicleId);

        return PageResponse.from(vehicleReviewRepository.findByVehicleIdAndReviewStatus(
                vehicleId,
                VehicleReviewStatus.APPROVED,
                publicReviewPageRequest(page, size)
        ).map(PublicVehicleReviewResponse::from));
    }

    @Transactional(readOnly = true)
    public PageResponse<AdminVehicleReviewResponse> getAdminVehicleReviews(
            Integer page,
            Integer size,
            VehicleReviewStatus reviewStatus,
            Long vehicleId
    ) {
        return PageResponse.from(vehicleReviewRepository.findAll(
                vehicleReviewSpecification(reviewStatus, vehicleId),
                pageRequest(page, size)
        ).map(AdminVehicleReviewResponse::from));
    }

    @Transactional(readOnly = true)
    public AdminVehicleReviewResponse getAdminVehicleReview(Long id) {
        return vehicleReviewRepository.findById(id)
                .map(AdminVehicleReviewResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vehicle review not found."
                ));
    }

    public List<VehicleReviewStatusOptionResponse> getAdminVehicleReviewStatusOptions() {
        return Arrays.stream(VehicleReviewStatus.values())
                .map(status -> new VehicleReviewStatusOptionResponse(status.name(), label(status)))
                .toList();
    }

    @Transactional
    public AdminVehicleReviewResponse updateAdminVehicleReviewStatus(
            Long id,
            UpdateVehicleReviewStatusRequest request,
            String updatedBy
    ) {
        VehicleReview vehicleReview = vehicleReviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vehicle review not found."
                ));

        OffsetDateTime now = OffsetDateTime.now();
        String safeUpdatedBy = StringUtil.trimToNull(updatedBy);
        vehicleReview.setReviewStatus(request.reviewStatus());
        vehicleReview.setModerationReason(StringUtil.trimToNull(request.moderationReason()));
        vehicleReview.setModeratedAt(now);
        vehicleReview.setModeratedBy(safeUpdatedBy);
        vehicleReview.setUpdatedAt(now);
        vehicleReview.setUpdatedBy(safeUpdatedBy);

        return AdminVehicleReviewResponse.from(vehicleReview);
    }

    private Specification<VehicleReview> vehicleReviewSpecification(
            VehicleReviewStatus reviewStatus,
            Long vehicleId
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (reviewStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("reviewStatus"), reviewStatus));
            }
            if (vehicleId != null) {
                predicates.add(criteriaBuilder.equal(root.get("vehicleId"), vehicleId));
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

    private PageRequest publicReviewPageRequest(Integer page, Integer size) {
        int safePage = page == null || page < 0 ? 0 : page;
        int safeSize = size == null || size < 1
                ? DEFAULT_PUBLIC_REVIEW_PAGE_SIZE
                : Math.min(size, MAX_PUBLIC_REVIEW_PAGE_SIZE);
        return PageRequest.of(safePage, safeSize, Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.desc("id")
        ));
    }

    private BigDecimal roundAverage(Double averageRating) {
        if (averageRating == null) {
            return null;
        }
        return BigDecimal.valueOf(averageRating).setScale(1, RoundingMode.HALF_UP);
    }

    private String label(VehicleReviewExperienceType experienceType) {
        String[] words = experienceType.name().toLowerCase(Locale.ROOT).split("_");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase(Locale.ROOT) + words[i].substring(1);
        }
        return String.join(" ", words);
    }

    private String label(VehicleReviewStatus status) {
        String[] words = status.name().toLowerCase(Locale.ROOT).split("_");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase(Locale.ROOT) + words[i].substring(1);
        }
        return String.join(" ", words);
    }
}
