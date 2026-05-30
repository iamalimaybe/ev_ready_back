package com.ev.ready.admin.charger.controller;

import com.ev.ready.charger.dto.AdminChargerResponse;
import com.ev.ready.charger.dto.AdminChargerFormOptionsResponse;
import com.ev.ready.charger.dto.CreateChargerRequest;
import com.ev.ready.charger.dto.UpdateChargerRequest;
import com.ev.ready.charger.enums.ChargerStatus;
import com.ev.ready.charger.service.ChargerAdminService;
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
@RequestMapping("/api/v1/admin/chargers")
public class AdminChargerController {

    private final ChargerAdminService chargerAdminService;

    public AdminChargerController(ChargerAdminService chargerAdminService) {
        this.chargerAdminService = chargerAdminService;
    }

    @GetMapping
    public PageResponse<AdminChargerResponse> getChargers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) ChargerStatus status,
            @RequestParam(required = false) VerificationStatus verificationStatus
    ) {
        return chargerAdminService.getAdminChargers(page, size, active, city, status, verificationStatus);
    }

    @GetMapping("/form-options")
    public AdminChargerFormOptionsResponse getChargerFormOptions() {
        return chargerAdminService.getAdminChargerFormOptions();
    }

    @GetMapping("/{id}")
    public AdminChargerResponse getCharger(@PathVariable Long id) {
        return chargerAdminService.getAdminCharger(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminChargerResponse createCharger(
            @Valid @RequestBody CreateChargerRequest request,
            Authentication authentication
    ) {
        return chargerAdminService.createAdminCharger(request, authentication.getName());
    }

    @PatchMapping("/{id}")
    public AdminChargerResponse updateCharger(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChargerRequest request,
            Authentication authentication
    ) {
        return chargerAdminService.updateAdminCharger(id, request, authentication.getName());
    }
}
