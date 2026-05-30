package com.ev.ready.charger.dto;

import com.ev.ready.catalog.dto.ChargerTypeResponse;
import java.util.List;

public record AdminChargerFormOptionsResponse(
        List<ChargerTypeResponse> chargerTypes,
        List<OptionResponse> chargingTypes,
        List<OptionResponse> chargerStatuses,
        List<OptionResponse> verificationStatuses
) {
}
