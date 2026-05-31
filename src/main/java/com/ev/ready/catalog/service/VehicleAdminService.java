package com.ev.ready.catalog.service;

import com.ev.ready.catalog.domain.Brand;
import com.ev.ready.catalog.domain.ChargerType;
import com.ev.ready.catalog.domain.Vehicle;
import com.ev.ready.catalog.dto.AdminVehicleFormOptionsResponse;
import com.ev.ready.catalog.dto.AdminVehicleResponse;
import com.ev.ready.catalog.dto.CreateVehicleRequest;
import com.ev.ready.catalog.dto.UpdateVehicleRequest;
import com.ev.ready.catalog.enums.VehicleType;
import com.ev.ready.catalog.repository.VehicleRepository;
import com.ev.ready.charger.dto.OptionResponse;
import com.ev.ready.common.PageResponse;
import com.ev.ready.common.enums.VerificationStatus;
import com.ev.ready.common.util.StringUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VehicleAdminService {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final VehicleRepository vehicleRepository;
    private final BrandReadService brandReadService;
    private final ChargerTypeReadService chargerTypeReadService;

    public VehicleAdminService(
            VehicleRepository vehicleRepository,
            BrandReadService brandReadService,
            ChargerTypeReadService chargerTypeReadService
    ) {
        this.vehicleRepository = vehicleRepository;
        this.brandReadService = brandReadService;
        this.chargerTypeReadService = chargerTypeReadService;
    }

    @Transactional(readOnly = true)
    public PageResponse<AdminVehicleResponse> getAdminVehicles(
            Integer page,
            Integer size,
            Boolean active,
            VehicleType type,
            Long brandId,
            Long chargerTypeId,
            VerificationStatus verificationStatus
    ) {
        return PageResponse.from(vehicleRepository.findAll(
                vehicleSpecification(active, type, brandId, chargerTypeId, verificationStatus),
                pageRequest(page, size)
        ).map(AdminVehicleResponse::from));
    }

    @Transactional(readOnly = true)
    public AdminVehicleResponse getAdminVehicle(Long id) {
        return vehicleRepository.findById(id)
                .map(AdminVehicleResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vehicle not found."
                ));
    }

    @Transactional(readOnly = true)
    public AdminVehicleFormOptionsResponse getAdminVehicleFormOptions() {
        return new AdminVehicleFormOptionsResponse(
                brandReadService.getBrands(),
                chargerTypeReadService.getChargerTypes(),
                enumOptions(VehicleType.values()),
                enumOptions(VerificationStatus.values())
        );
    }

    @Transactional
    public AdminVehicleResponse createAdminVehicle(
            CreateVehicleRequest request,
            String createdBy
    ) {
        Vehicle vehicle = new Vehicle();
        vehicle.setCreatedAt(OffsetDateTime.now());
        vehicle.setCreatedBy(StringUtil.trimToNull(createdBy));
        vehicle.setActive(request.active() == null || request.active());

        vehicle.setVehicleType(request.type());
        vehicle.setBrand(brandReadService.getActiveBrand(request.brandId()));
        vehicle.setChargerType(resolveActiveChargerType(request.chargerTypeId()));
        vehicle.setModel(requiredText(request.model(), "Model is required."));
        vehicle.setVariant(StringUtil.trimToNull(request.variant()));
        vehicle.setPricePkr(request.pricePkr());
        vehicle.setRangeKm(request.rangeKm());
        vehicle.setBatteryCapacityKwh(request.batteryCapacityKwh());
        vehicle.setDcFastCharging(request.dcFastCharging() != null && request.dcFastCharging());
        vehicle.setImage(StringUtil.trimToNull(request.image()));
        vehicle.setDescription(StringUtil.trimToNull(request.description()));
        vehicle.setSourceUrl(StringUtil.trimToNull(request.sourceUrl()));
        vehicle.setSourceLabel(StringUtil.trimToNull(request.sourceLabel()));
        vehicle.setSourceCheckedAt(request.sourceCheckedAt());
        vehicle.setVerificationStatus(request.verificationStatus());
        vehicle.setDisplayOrder(request.displayOrder());

        return AdminVehicleResponse.from(vehicleRepository.save(vehicle));
    }

    @Transactional
    public AdminVehicleResponse updateAdminVehicle(
            Long id,
            UpdateVehicleRequest request,
            String updatedBy
    ) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vehicle not found."
                ));

        vehicle.setVehicleType(request.type());
        vehicle.setBrand(brandReadService.getActiveBrand(request.brandId()));
        vehicle.setChargerType(resolveActiveChargerType(request.chargerTypeId()));
        vehicle.setModel(requiredText(request.model(), "Model is required."));
        vehicle.setVariant(StringUtil.trimToNull(request.variant()));
        vehicle.setPricePkr(request.pricePkr());
        vehicle.setRangeKm(request.rangeKm());
        vehicle.setBatteryCapacityKwh(request.batteryCapacityKwh());
        vehicle.setDcFastCharging(request.dcFastCharging());
        vehicle.setImage(StringUtil.trimToNull(request.image()));
        vehicle.setDescription(StringUtil.trimToNull(request.description()));
        vehicle.setSourceUrl(StringUtil.trimToNull(request.sourceUrl()));
        vehicle.setSourceLabel(StringUtil.trimToNull(request.sourceLabel()));
        vehicle.setSourceCheckedAt(request.sourceCheckedAt());
        vehicle.setVerificationStatus(request.verificationStatus());
        vehicle.setActive(request.active());
        vehicle.setDisplayOrder(request.displayOrder());

        vehicle.setUpdatedAt(OffsetDateTime.now());
        vehicle.setUpdatedBy(StringUtil.trimToNull(updatedBy));

        return AdminVehicleResponse.from(vehicle);
    }

    private Specification<Vehicle> vehicleSpecification(
            Boolean active,
            VehicleType type,
            Long brandId,
            Long chargerTypeId,
            VerificationStatus verificationStatus
    ) {
        return (root, query, criteriaBuilder) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("brand", JoinType.INNER);
                root.fetch("chargerType", JoinType.LEFT);
            }
            query.distinct(true);

            Join<Vehicle, Brand> brand = root.join("brand", JoinType.INNER);
            Join<Vehicle, ChargerType> chargerType = root.join("chargerType", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();
            if (active != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), active));
            }
            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("vehicleType"), type));
            }
            if (brandId != null) {
                predicates.add(criteriaBuilder.equal(brand.get("id"), brandId));
            }
            if (chargerTypeId != null) {
                predicates.add(criteriaBuilder.equal(chargerType.get("id"), chargerTypeId));
            }
            if (verificationStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("verificationStatus"), verificationStatus));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private PageRequest pageRequest(Integer page, Integer size) {
        int safePage = page == null || page < 0 ? 0 : page;
        int safeSize = size == null || size < 1 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
        return PageRequest.of(safePage, safeSize, Sort.by(
                Sort.Order.asc("displayOrder"),
                Sort.Order.asc("id")
        ));
    }

    private ChargerType resolveActiveChargerType(Long id) {
        if (id == null) {
            return null;
        }
        return chargerTypeReadService.getActiveChargerType(id);
    }

    private String requiredText(String value, String message) {
        String trimmedValue = StringUtil.trimToNull(value);
        if (trimmedValue == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        return trimmedValue;
    }

    private <E extends Enum<E>> List<OptionResponse> enumOptions(E[] values) {
        return Arrays.stream(values)
                .map(value -> new OptionResponse(value.name(), label(value.name())))
                .toList();
    }

    private String label(String value) {
        String[] words = value.toLowerCase(Locale.ROOT).split("_");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase(Locale.ROOT) + words[i].substring(1);
        }
        return String.join(" ", words);
    }
}
