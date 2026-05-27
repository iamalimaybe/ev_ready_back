package com.ev.ready.charger.controller;

import com.ev.ready.charger.dto.ChargerResponse;
import com.ev.ready.charger.service.ChargerReadService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chargers")
public class ChargerController {

    private final ChargerReadService chargerReadService;

    public ChargerController(ChargerReadService chargerReadService) {
        this.chargerReadService = chargerReadService;
    }

    @GetMapping
    public List<ChargerResponse> getChargers(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Long chargerTypeId,
            @RequestParam(required = false) String chargingType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sort
    ) {
        return chargerReadService.getChargers(city, chargerTypeId, chargingType, status, sort);
    }

    @GetMapping("/cities")
    public List<String> getChargerCities() {
        return chargerReadService.getChargerCities();
    }

    @GetMapping("/{id}")
    public ChargerResponse getCharger(@PathVariable Long id) {
        return chargerReadService.getCharger(id);
    }
}
