package com.ev.ready.charger.dto;

import com.ev.ready.catalog.domain.ChargerType;
import com.ev.ready.charger.domain.Charger;
import com.ev.ready.charger.enums.ChargerStatus;
import com.ev.ready.charger.enums.ChargingType;
import com.ev.ready.common.enums.VerificationStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AdminChargerResponse(
        Long id,
        ChargerTypeDisplay chargerType,
        String name,
        String city,
        String area,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        ChargingType chargingType,
        ChargerStatus status,
        BigDecimal powerKw,
        String priceNote,
        String description,
        String image,
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

    public static AdminChargerResponse from(Charger charger) {
        return new AdminChargerResponse(
                charger.getId(),
                ChargerTypeDisplay.from(charger.getChargerType()),
                charger.getName(),
                charger.getCity(),
                charger.getArea(),
                charger.getAddress(),
                charger.getLatitude(),
                charger.getLongitude(),
                charger.getChargingType(),
                charger.getStatus(),
                charger.getPowerKw(),
                charger.getPriceNote(),
                charger.getDescription(),
                charger.getImage(),
                charger.getSourceUrl(),
                charger.getSourceLabel(),
                charger.getSourceCheckedAt(),
                charger.getVerificationStatus(),
                charger.getActive(),
                charger.getDisplayOrder(),
                charger.getCreatedAt(),
                charger.getCreatedBy(),
                charger.getUpdatedAt(),
                charger.getUpdatedBy()
        );
    }

    public record ChargerTypeDisplay(
            Long id,
            String code,
            String name
    ) {

        private static ChargerTypeDisplay from(ChargerType chargerType) {
            return new ChargerTypeDisplay(
                    chargerType.getId(),
                    chargerType.getCode(),
                    chargerType.getName()
            );
        }
    }
}
