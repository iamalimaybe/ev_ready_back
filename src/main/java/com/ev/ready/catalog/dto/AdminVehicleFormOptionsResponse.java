package com.ev.ready.catalog.dto;

import com.ev.ready.charger.dto.OptionResponse;
import java.util.List;

public record AdminVehicleFormOptionsResponse(
        List<BrandResponse> brands,
        List<ChargerTypeResponse> chargerTypes,
        List<OptionResponse> vehicleTypes,
        List<OptionResponse> verificationStatuses
) {
}
