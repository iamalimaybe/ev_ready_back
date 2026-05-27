package com.ev.ready.catalog.repository;

import com.ev.ready.catalog.domain.ChargerType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargerTypeRepository extends JpaRepository<ChargerType, Long> {

    List<ChargerType> findByActiveTrueOrderByDisplayOrderAscNameAsc();
}
