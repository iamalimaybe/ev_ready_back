package com.ev.ready.catalog.dto;

import com.ev.ready.catalog.domain.Brand;

public record BrandResponse(
        Long id,
        String name,
        String logo,
        String description,
        Integer displayOrder
) {

    public static BrandResponse from(Brand brand) {
        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getLogo(),
                brand.getDescription(),
                brand.getDisplayOrder()
        );
    }
}
