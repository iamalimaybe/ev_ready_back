package com.ev.ready.catalog.service;

import com.ev.ready.catalog.domain.Brand;
import com.ev.ready.catalog.dto.BrandResponse;
import com.ev.ready.catalog.repository.BrandRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Brand getActiveBrand(Long id) {
        return brandRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Brand must exist and be active."
                ));
    }
}
