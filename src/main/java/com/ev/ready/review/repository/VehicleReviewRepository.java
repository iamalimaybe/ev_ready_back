package com.ev.ready.review.repository;

import com.ev.ready.review.domain.VehicleReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleReviewRepository extends JpaRepository<VehicleReview, Long> {
}
