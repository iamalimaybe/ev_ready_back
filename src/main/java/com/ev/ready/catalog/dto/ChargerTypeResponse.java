package com.ev.ready.catalog.dto;

import com.ev.ready.catalog.domain.ChargerType;

public record ChargerTypeResponse(
        Long id,
        String code,
        String name,
        String description,
        Integer displayOrder
) {

    public static ChargerTypeResponse from(ChargerType chargerType) {
        return new ChargerTypeResponse(
                chargerType.getId(),
                chargerType.getCode(),
                chargerType.getName(),
                chargerType.getDescription(),
                chargerType.getDisplayOrder()
        );
    }
}
