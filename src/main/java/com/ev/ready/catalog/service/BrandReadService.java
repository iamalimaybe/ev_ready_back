package com.ev.ready.catalog.service;

import com.ev.ready.catalog.dto.BrandResponse;
import com.ev.ready.catalog.repository.BrandRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BrandReadService {

    private final BrandRepository brandRepository;

    public BrandReadService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandResponse> getBrands() {
        return brandRepository.findByActiveTrueOrderByDisplayOrderAscNameAsc()
                .stream()
                .map(BrandResponse::from)
                .toList();
    }
}
