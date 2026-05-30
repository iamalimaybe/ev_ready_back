package com.ev.ready.catalog.service;

import com.ev.ready.catalog.domain.Brand;
import com.ev.ready.catalog.domain.ChargerType;
import com.ev.ready.catalog.domain.Vehicle;
import com.ev.ready.catalog.dto.VehicleResponse;
import com.ev.ready.catalog.enums.VehicleType;
import com.ev.ready.catalog.repository.VehicleRepository;
import com.ev.ready.common.PageResponse;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class VehicleReadService {

    private static final int DEFAULT_PUBLIC_PAGE_SIZE = 6;
    private static final int MAX_PUBLIC_PAGE_SIZE = 50;

    private final VehicleRepository vehicleRepository;

    public VehicleReadService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<VehicleResponse> getVehicles(
            String type,
            Long brandId,
            Long chargerTypeId,
            Long priceMax,
            Integer rangeMin,
            Boolean dcFastCharging,
            String sort
    ) {
        VehicleType vehicleType = parseVehicleType(type);

        return vehicleRepository.findAll(
                        vehicleSpecification(vehicleType, brandId, chargerTypeId, priceMax, rangeMin, dcFastCharging),
                        vehicleSort(sort)
                )
                .stream()
                .map(VehicleResponse::from)
                .toList();
    }

    public PageResponse<VehicleResponse> getVehicles(
            String type,
            Long brandId,
            Long chargerTypeId,
            Long priceMax,
            Integer rangeMin,
            Boolean dcFastCharging,
            String sort,
            Integer page,
            Integer size
    ) {
        VehicleType vehicleType = parseVehicleType(type);
        PageRequest pageRequest = PageRequest.of(
                normalizedPage(page),
                normalizedSize(size),
                vehicleSort(sort)
        );

        return PageResponse.from(vehicleRepository.findAll(
                vehicleSpecification(vehicleType, brandId, chargerTypeId, priceMax, rangeMin, dcFastCharging),
                pageRequest
        ).map(VehicleResponse::from));
    }

    public VehicleResponse getVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findPublicById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Vehicle not found"));
        return VehicleResponse.from(vehicle);
    }

    public void ensurePublicVehicleExists(Long id) {
        if (vehicleRepository.findPublicById(id).isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Vehicle not found");
        }
    }

    private Specification<Vehicle> vehicleSpecification(
            VehicleType type,
            Long brandId,
            Long chargerTypeId,
            Long priceMax,
            Integer rangeMin,
            Boolean dcFastCharging
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
            predicates.add(criteriaBuilder.isTrue(root.get("active")));
            predicates.add(criteriaBuilder.isTrue(brand.get("active")));
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.isNull(root.get("chargerType")),
                    criteriaBuilder.isTrue(chargerType.get("active"))
            ));

            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("vehicleType"), type));
            }
            if (brandId != null) {
                predicates.add(criteriaBuilder.equal(brand.get("id"), brandId));
            }
            if (chargerTypeId != null) {
                predicates.add(criteriaBuilder.equal(chargerType.get("id"), chargerTypeId));
            }
            if (priceMax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("pricePkr"), priceMax));
            }
            if (rangeMin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rangeKm"), rangeMin));
            }
            if (dcFastCharging != null) {
                predicates.add(criteriaBuilder.equal(root.get("dcFastCharging"), dcFastCharging));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private VehicleType parseVehicleType(String type) {
        if (type == null || type.isBlank()) {
            return null;
        }
        try {
            return VehicleType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(BAD_REQUEST, "Invalid vehicle type");
        }
    }

    private Sort vehicleSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return defaultVehicleSort();
        }
        return switch (sort) {
            case "priceAsc" -> Sort.by(Sort.Order.asc("pricePkr"), Sort.Order.asc("id"));
            case "priceDesc" -> Sort.by(Sort.Order.desc("pricePkr"), Sort.Order.asc("id"));
            case "rangeAsc" -> Sort.by(Sort.Order.asc("rangeKm"), Sort.Order.asc("id"));
            case "rangeDesc" -> Sort.by(Sort.Order.desc("rangeKm"), Sort.Order.asc("id"));
            default -> defaultVehicleSort();
        };
    }

    private Sort defaultVehicleSort() {
        return Sort.by(Sort.Order.asc("displayOrder"), Sort.Order.asc("id"));
    }

    private int normalizedPage(Integer page) {
        if (page == null) {
            return 0;
        }
        return Math.max(page, 0);
    }

    private int normalizedSize(Integer size) {
        if (size == null) {
            return DEFAULT_PUBLIC_PAGE_SIZE;
        }
        return Math.min(Math.max(size, 1), MAX_PUBLIC_PAGE_SIZE);
    }
}
