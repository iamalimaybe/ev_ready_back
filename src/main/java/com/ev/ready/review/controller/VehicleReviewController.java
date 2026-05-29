package com.ev.ready.review.controller;

import com.ev.ready.review.dto.CreateVehicleReviewRequest;
import com.ev.ready.review.dto.VehicleReviewExperienceTypeOptionResponse;
import com.ev.ready.review.dto.VehicleReviewSubmissionResponse;
import com.ev.ready.review.service.VehicleReviewService;
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
@RequestMapping("/api/v1/vehicles")
public class VehicleReviewController {

    private final VehicleReviewService vehicleReviewService;

    public VehicleReviewController(VehicleReviewService vehicleReviewService) {
        this.vehicleReviewService = vehicleReviewService;
    }

    @GetMapping("/reviews/experience-types")
    public List<VehicleReviewExperienceTypeOptionResponse> getExperienceTypeOptions() {
        return vehicleReviewService.getExperienceTypeOptions();
    }

    @PostMapping("/{vehicleId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleReviewSubmissionResponse createVehicleReview(
            @PathVariable Long vehicleId,
            @Valid @RequestBody CreateVehicleReviewRequest request
    ) {
        return vehicleReviewService.createVehicleReview(vehicleId, request);
    }
}
