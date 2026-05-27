package com.ev.ready.catalog.dto;

import com.ev.ready.catalog.domain.Brand;
import com.ev.ready.catalog.domain.ChargerType;
import com.ev.ready.catalog.domain.Vehicle;
import com.ev.ready.catalog.enums.VehicleType;
import com.ev.ready.common.enums.VerificationStatus;

import java.math.BigDecimal;

public record VehicleResponse(
        Long id,
        VehicleType vehicleType,
        BrandDisplay brand,
        ChargerTypeDisplay chargerType,
        String model,
        String variant,
        Long pricePkr,
        Integer rangeKm,
        BigDecimal batteryCapacityKwh,
        Boolean dcFastCharging,
        String image,
        String description,
        Integer displayOrder,
        VerificationStatus verificationStatus
) {

    public static VehicleResponse from(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getVehicleType(),
                BrandDisplay.from(vehicle.getBrand()),
                ChargerTypeDisplay.from(vehicle.getChargerType()),
                vehicle.getModel(),
                vehicle.getVariant(),
                vehicle.getPricePkr(),
                vehicle.getRangeKm(),
                vehicle.getBatteryCapacityKwh(),
                vehicle.getDcFastCharging(),
                vehicle.getImage(),
                vehicle.getDescription(),
                vehicle.getDisplayOrder(),
                vehicle.getVerificationStatus()
        );
    }

    public record BrandDisplay(
            Long id,
            String name,
            String logo
    ) {

        private static BrandDisplay from(Brand brand) {
            return new BrandDisplay(brand.getId(), brand.getName(), brand.getLogo());
        }
    }

    public record ChargerTypeDisplay(
            Long id,
            String code,
            String name
    ) {

        private static ChargerTypeDisplay from(ChargerType chargerType) {
            if (chargerType == null) {
                return null;
            }
            return new ChargerTypeDisplay(chargerType.getId(), chargerType.getCode(), chargerType.getName());
        }
    }
}
