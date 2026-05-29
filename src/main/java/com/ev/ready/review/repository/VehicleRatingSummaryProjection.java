package com.ev.ready.review.repository;

public interface VehicleRatingSummaryProjection {

    Long getVehicleId();

    Double getAverageRating();

    Long getRatingCount();
}
