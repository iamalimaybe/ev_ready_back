package com.ev.ready.catalog.controller;

import com.ev.ready.catalog.dto.VehicleResponse;
import com.ev.ready.catalog.service.VehicleReadService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final VehicleReadService vehicleReadService;

    public VehicleController(VehicleReadService vehicleReadService) {
        this.vehicleReadService = vehicleReadService;
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
        return vehicleReadService.getVehicles(
                type,
                brandId,
                chargerTypeId,
                priceMax,
                rangeMin,
                dcFastCharging,
                sort
        );
    }

    @GetMapping("/{id}")
    public VehicleResponse getVehicle(@PathVariable Long id) {
        return vehicleReadService.getVehicle(id);
    }
}
