package com.ev.ready.admin.vehicle.controller;

import com.ev.ready.catalog.dto.AdminVehicleFormOptionsResponse;
import com.ev.ready.catalog.dto.AdminVehicleResponse;
import com.ev.ready.catalog.dto.CreateVehicleRequest;
import com.ev.ready.catalog.dto.UpdateVehicleRequest;
import com.ev.ready.catalog.enums.VehicleType;
import com.ev.ready.catalog.service.VehicleAdminService;
import com.ev.ready.common.PageResponse;
import com.ev.ready.common.enums.VerificationStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/vehicles")
public class AdminVehicleController {

    private final VehicleAdminService vehicleAdminService;

    public AdminVehicleController(VehicleAdminService vehicleAdminService) {
        this.vehicleAdminService = vehicleAdminService;
    }

    @GetMapping
    public PageResponse<AdminVehicleResponse> getVehicles(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) VehicleType type,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long chargerTypeId,
            @RequestParam(required = false) VerificationStatus verificationStatus
    ) {
        return vehicleAdminService.getAdminVehicles(
                page,
                size,
                active,
                type,
                brandId,
                chargerTypeId,
                verificationStatus
        );
    }

    @GetMapping("/form-options")
    public AdminVehicleFormOptionsResponse getVehicleFormOptions() {
        return vehicleAdminService.getAdminVehicleFormOptions();
    }

    @GetMapping("/{id}")
    public AdminVehicleResponse getVehicle(@PathVariable Long id) {
        return vehicleAdminService.getAdminVehicle(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminVehicleResponse createVehicle(
            @Valid @RequestBody CreateVehicleRequest request,
            Authentication authentication
    ) {
        return vehicleAdminService.createAdminVehicle(request, authentication.getName());
    }

    @PatchMapping("/{id}")
    public AdminVehicleResponse updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleRequest request,
            Authentication authentication
    ) {
        return vehicleAdminService.updateAdminVehicle(id, request, authentication.getName());
    }
}
