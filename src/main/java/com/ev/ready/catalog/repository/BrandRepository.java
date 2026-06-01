package com.ev.ready.catalog.repository;

import com.ev.ready.catalog.domain.Brand;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    List<Brand> findByActiveTrueOrderByDisplayOrderAscNameAsc();

    Optional<Brand> findByIdAndActiveTrue(Long id);
}
