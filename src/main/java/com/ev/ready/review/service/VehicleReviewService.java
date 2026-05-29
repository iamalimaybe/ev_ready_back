package com.ev.ready.review.service;

import com.ev.ready.catalog.service.VehicleReadService;
import com.ev.ready.common.util.StringUtil;
import com.ev.ready.review.domain.VehicleReview;
import com.ev.ready.review.dto.CreateVehicleReviewRequest;
import com.ev.ready.review.dto.VehicleReviewExperienceTypeOptionResponse;
import com.ev.ready.review.dto.VehicleReviewSubmissionResponse;
import com.ev.ready.review.enums.VehicleReviewExperienceType;
import com.ev.ready.review.enums.VehicleReviewStatus;
import com.ev.ready.review.repository.VehicleReviewRepository;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleReviewService {

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

    private String label(VehicleReviewExperienceType experienceType) {
        String[] words = experienceType.name().toLowerCase(Locale.ROOT).split("_");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase(Locale.ROOT) + words[i].substring(1);
        }
        return String.join(" ", words);
    }
}
