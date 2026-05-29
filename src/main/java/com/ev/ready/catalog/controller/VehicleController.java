package com.ev.ready.catalog.controller;

import com.ev.ready.catalog.dto.VehicleResponse;
import com.ev.ready.catalog.service.VehicleReadService;
import com.ev.ready.review.dto.VehicleRatingSummaryResponse;
import com.ev.ready.review.service.VehicleReviewService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final VehicleReadService vehicleReadService;
    private final VehicleReviewService vehicleReviewService;

    public VehicleController(
            VehicleReadService vehicleReadService,
            VehicleReviewService vehicleReviewService
    ) {
        this.vehicleReadService = vehicleReadService;
        this.vehicleReviewService = vehicleReviewService;
    }

    @GetMapping
    public List<VehicleResponse> getVehicles(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long chargerTypeId,
            @RequestParam(required = false) Long priceMax,
            @RequestParam(required = false) Integer rangeMin,
            @RequestParam(required = false) Boolean dcFastCharging,
            @RequestParam(required = false) String sort
    ) {
        List<VehicleResponse> vehicles = vehicleReadService.getVehicles(
                type,
                brandId,
                chargerTypeId,
                priceMax,
                rangeMin,
                dcFastCharging,
                sort
        );
        Map<Long, VehicleRatingSummaryResponse> ratingSummaries =
                vehicleReviewService.getApprovedRatingSummaries(vehicles.stream()
                        .map(VehicleResponse::id)
                        .toList());

        return vehicles.stream()
                .map(vehicle -> vehicle.withRatingSummary(ratingSummaries.getOrDefault(
                        vehicle.id(),
                        VehicleRatingSummaryResponse.empty()
                )))
                .toList();
    }

    @GetMapping("/{id}")
    public VehicleResponse getVehicle(@PathVariable Long id) {
        VehicleResponse vehicle = vehicleReadService.getVehicle(id);
        return vehicle.withRatingSummary(vehicleReviewService.getApprovedRatingSummary(id));
    }
}
