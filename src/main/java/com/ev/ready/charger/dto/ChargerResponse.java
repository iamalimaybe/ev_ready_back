package com.ev.ready.charger.dto;

import com.ev.ready.catalog.domain.ChargerType;
import com.ev.ready.charger.domain.Charger;
import com.ev.ready.charger.enums.ChargerStatus;
import com.ev.ready.charger.enums.ChargingType;
import com.ev.ready.common.enums.VerificationStatus;

import java.math.BigDecimal;

public record ChargerResponse(
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
        Integer displayOrder,
        VerificationStatus verificationStatus
) {

    public static ChargerResponse from(Charger charger) {
        return new ChargerResponse(
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
                charger.getDisplayOrder(),
                charger.getVerificationStatus()
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
