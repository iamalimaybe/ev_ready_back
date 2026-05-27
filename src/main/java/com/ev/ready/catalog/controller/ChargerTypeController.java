package com.ev.ready.catalog.controller;

import com.ev.ready.catalog.dto.ChargerTypeResponse;
import com.ev.ready.catalog.service.ChargerTypeReadService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/charger-types")
public class ChargerTypeController {

    private final ChargerTypeReadService chargerTypeReadService;

    public ChargerTypeController(ChargerTypeReadService chargerTypeReadService) {
        this.chargerTypeReadService = chargerTypeReadService;
    }

    @GetMapping
    public List<ChargerTypeResponse> getChargerTypes() {
        return chargerTypeReadService.getChargerTypes();
    }
}
