package com.ev.ready.review.repository;

import com.ev.ready.review.domain.VehicleReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VehicleReviewRepository extends JpaRepository<VehicleReview, Long>, JpaSpecificationExecutor<VehicleReview> {
}
