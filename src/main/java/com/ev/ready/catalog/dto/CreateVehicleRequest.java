package com.ev.ready.catalog.dto;

import com.ev.ready.catalog.enums.VehicleType;
import com.ev.ready.common.enums.VerificationStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreateVehicleRequest(
        @NotNull(message = "Vehicle type is required.")
        VehicleType type,

        @NotNull(message = "Brand is required.")
        Long brandId,

        Long chargerTypeId,

        @NotBlank(message = "Model is required.")
        @Size(max = 150, message = "Model must be 150 characters or fewer.")
        String model,

        @Size(max = 150, message = "Variant must be 150 characters or fewer.")
        String variant,

        @Min(value = 0, message = "Price PKR must be 0 or greater.")
        Long pricePkr,

        @Min(value = 1, message = "Range km must be greater than 0.")
        Integer rangeKm,

        @DecimalMin(value = "0.0", inclusive = false, message = "Battery capacity must be greater than 0.")
        BigDecimal batteryCapacityKwh,

        Boolean dcFastCharging,

        @Size(max = 255, message = "Image must be 255 characters or fewer.")
        String image,

        String description,

        @Size(max = 500, message = "Source URL must be 500 characters or fewer.")
        String sourceUrl,

        @Size(max = 150, message = "Source label must be 150 characters or fewer.")
        String sourceLabel,

        OffsetDateTime sourceCheckedAt,

        @NotNull(message = "Verification status is required.")
        VerificationStatus verificationStatus,

        Boolean active,

        @NotNull(message = "Display order is required.")
        @Min(value = 0, message = "Display order must be 0 or greater.")
        Integer displayOrder
) {
}
