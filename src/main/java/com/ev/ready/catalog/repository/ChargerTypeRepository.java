package com.ev.ready.catalog.repository;

import com.ev.ready.catalog.domain.ChargerType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargerTypeRepository extends JpaRepository<ChargerType, Long> {

    List<ChargerType> findByActiveTrueOrderByDisplayOrderAscNameAsc();

    Optional<ChargerType> findByIdAndActiveTrue(Long id);
}
