package com.ev.ready.catalog.dto;

import com.ev.ready.catalog.domain.Brand;
import com.ev.ready.catalog.domain.ChargerType;
import com.ev.ready.catalog.domain.Vehicle;
import com.ev.ready.catalog.enums.VehicleType;
import com.ev.ready.common.enums.VerificationStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AdminVehicleResponse(
        Long id,
        VehicleType type,
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
        String sourceUrl,
        String sourceLabel,
        OffsetDateTime sourceCheckedAt,
        VerificationStatus verificationStatus,
        Boolean active,
        Integer displayOrder,
        OffsetDateTime createdAt,
        String createdBy,
        OffsetDateTime updatedAt,
        String updatedBy
) {

    public static AdminVehicleResponse from(Vehicle vehicle) {
        return new AdminVehicleResponse(
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
                vehicle.getSourceUrl(),
                vehicle.getSourceLabel(),
                vehicle.getSourceCheckedAt(),
                vehicle.getVerificationStatus(),
                vehicle.getActive(),
                vehicle.getDisplayOrder(),
                vehicle.getCreatedAt(),
                vehicle.getCreatedBy(),
                vehicle.getUpdatedAt(),
                vehicle.getUpdatedBy()
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
            return new ChargerTypeDisplay(
                    chargerType.getId(),
                    chargerType.getCode(),
                    chargerType.getName()
            );
        }
    }
}
