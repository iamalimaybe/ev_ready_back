package com.ev.ready.catalog.controller;

import com.ev.ready.catalog.dto.BrandResponse;
import com.ev.ready.catalog.service.BrandReadService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {

    private final BrandReadService brandReadService;

    public BrandController(BrandReadService brandReadService) {
        this.brandReadService = brandReadService;
    }

    @GetMapping
    public List<BrandResponse> getBrands() {
        return brandReadService.getBrands();
    }
}
