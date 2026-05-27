package com.ev.ready.charger.repository;

import com.ev.ready.charger.domain.Charger;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChargerRepository extends JpaRepository<Charger, Long>, JpaSpecificationExecutor<Charger> {

    @EntityGraph(attributePaths = "chargerType")
    @Query("""
            SELECT c
            FROM Charger c
            JOIN c.chargerType ct
            WHERE c.id = :id
              AND c.active = true
              AND ct.active = true
            """)
    Optional<Charger> findPublicById(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT c.city
            FROM Charger c
            JOIN c.chargerType ct
            WHERE c.active = true
              AND ct.active = true
            ORDER BY c.city ASC
            """)
    List<String> findDistinctPublicCities();
}
