package com.ev.ready.charger.dto;

import com.ev.ready.charger.enums.ChargerStatus;
import com.ev.ready.charger.enums.ChargingType;
import com.ev.ready.common.enums.VerificationStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record UpdateChargerRequest(
        @NotNull(message = "Charger type is required.")
        Long chargerTypeId,

        @NotBlank(message = "Name is required.")
        @Size(max = 150, message = "Name must be 150 characters or fewer.")
        String name,

        @NotBlank(message = "City is required.")
        @Size(max = 100, message = "City must be 100 characters or fewer.")
        String city,

        @Size(max = 150, message = "Area must be 150 characters or fewer.")
        String area,

        String address,

        @DecimalMin(value = "-90.0", message = "Latitude must be at least -90.")
        @DecimalMax(value = "90.0", message = "Latitude must be at most 90.")
        BigDecimal latitude,

        @DecimalMin(value = "-180.0", message = "Longitude must be at least -180.")
        @DecimalMax(value = "180.0", message = "Longitude must be at most 180.")
        BigDecimal longitude,

        @NotNull(message = "Charging type is required.")
        ChargingType chargingType,

        @NotNull(message = "Charger status is required.")
        ChargerStatus status,

        @DecimalMin(value = "0.0", inclusive = false, message = "Power must be greater than 0.")
        BigDecimal powerKw,

        @Size(max = 255, message = "Price note must be 255 characters or fewer.")
        String priceNote,

        @NotBlank(message = "Description is required.")
        String description,

        @Size(max = 255, message = "Image must be 255 characters or fewer.")
        String image,

        @Size(max = 500, message = "Source URL must be 500 characters or fewer.")
        String sourceUrl,

        @Size(max = 150, message = "Source label must be 150 characters or fewer.")
        String sourceLabel,

        OffsetDateTime sourceCheckedAt,

        @NotNull(message = "Verification status is required.")
        VerificationStatus verificationStatus,

        @NotNull(message = "Active flag is required.")
        Boolean active,

        @NotNull(message = "Display order is required.")
        @Min(value = 0, message = "Display order must be 0 or greater.")
        Integer displayOrder
) {
}
