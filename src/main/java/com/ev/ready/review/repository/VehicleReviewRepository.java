package com.ev.ready.review.repository;

import com.ev.ready.review.domain.VehicleReview;
import com.ev.ready.review.enums.VehicleReviewStatus;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehicleReviewRepository extends JpaRepository<VehicleReview, Long>, JpaSpecificationExecutor<VehicleReview> {

    Page<VehicleReview> findByVehicleIdAndReviewStatus(
            Long vehicleId,
            VehicleReviewStatus reviewStatus,
            Pageable pageable
    );

    @Query("""
            SELECT vr.vehicleId AS vehicleId,
                   AVG(vr.rating) AS averageRating,
                   COUNT(vr.id) AS ratingCount
            FROM VehicleReview vr
            WHERE vr.reviewStatus = :reviewStatus
              AND vr.vehicleId IN :vehicleIds
            GROUP BY vr.vehicleId
            """)
    List<VehicleRatingSummaryProjection> findRatingSummaries(
            @Param("vehicleIds") Collection<Long> vehicleIds,
            @Param("reviewStatus") VehicleReviewStatus reviewStatus
    );
}
