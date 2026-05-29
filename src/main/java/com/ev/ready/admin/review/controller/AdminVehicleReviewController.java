package com.ev.ready.admin.review.controller;

import com.ev.ready.common.PageResponse;
import com.ev.ready.review.dto.AdminVehicleReviewResponse;
import com.ev.ready.review.dto.UpdateVehicleReviewStatusRequest;
import com.ev.ready.review.dto.VehicleReviewStatusOptionResponse;
import com.ev.ready.review.enums.VehicleReviewStatus;
import com.ev.ready.review.service.VehicleReviewService;
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
@RequestMapping("/api/v1/admin/vehicle-reviews")
public class AdminVehicleReviewController {

    private final VehicleReviewService vehicleReviewService;

    public AdminVehicleReviewController(VehicleReviewService vehicleReviewService) {
        this.vehicleReviewService = vehicleReviewService;
    }

    @GetMapping
    public PageResponse<AdminVehicleReviewResponse> getVehicleReviews(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) VehicleReviewStatus reviewStatus,
            @RequestParam(required = false) Long vehicleId
    ) {
        return vehicleReviewService.getAdminVehicleReviews(page, size, reviewStatus, vehicleId);
    }

    @GetMapping("/statuses")
    public List<VehicleReviewStatusOptionResponse> getVehicleReviewStatusOptions() {
        return vehicleReviewService.getAdminVehicleReviewStatusOptions();
    }

    @GetMapping("/{id}")
    public AdminVehicleReviewResponse getVehicleReview(@PathVariable Long id) {
        return vehicleReviewService.getAdminVehicleReview(id);
    }

    @PatchMapping("/{id}/status")
    public AdminVehicleReviewResponse updateVehicleReviewStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleReviewStatusRequest request,
            Authentication authentication
    ) {
        return vehicleReviewService.updateAdminVehicleReviewStatus(id, request, authentication.getName());
    }
}
