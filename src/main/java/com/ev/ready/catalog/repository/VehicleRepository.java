package com.ev.ready.catalog.repository;

import com.ev.ready.catalog.domain.Vehicle;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {

    @EntityGraph(attributePaths = {"brand", "chargerType"})
    @Query("""
            SELECT v
            FROM Vehicle v
            JOIN v.brand b
            LEFT JOIN v.chargerType ct
            WHERE v.id = :id
              AND v.active = true
              AND b.active = true
              AND (ct IS NULL OR ct.active = true)
            """)
    Optional<Vehicle> findPublicById(@Param("id") Long id);
}
