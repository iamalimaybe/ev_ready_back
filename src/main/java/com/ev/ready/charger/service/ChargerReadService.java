package com.ev.ready.charger.service;

import com.ev.ready.catalog.domain.ChargerType;
import com.ev.ready.charger.domain.Charger;
import com.ev.ready.charger.dto.ChargerResponse;
import com.ev.ready.charger.enums.ChargerStatus;
import com.ev.ready.charger.enums.ChargingType;
import com.ev.ready.charger.repository.ChargerRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ChargerReadService {

    private final ChargerRepository chargerRepository;

    public ChargerReadService(ChargerRepository chargerRepository) {
        this.chargerRepository = chargerRepository;
    }

    public List<ChargerResponse> getChargers(
            String city,
            Long chargerTypeId,
            String chargingType,
            String status,
            String sort
    ) {
        ChargingType parsedChargingType = parseChargingType(chargingType);
        ChargerStatus parsedStatus = parseStatus(status);

        return chargerRepository.findAll(
                        chargerSpecification(city, chargerTypeId, parsedChargingType, parsedStatus),
                        chargerSort(sort)
                )
                .stream()
                .map(ChargerResponse::from)
                .toList();
    }

    public ChargerResponse getCharger(Long id) {
        Charger charger = chargerRepository.findPublicById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Charger not found"));
        return ChargerResponse.from(charger);
    }

    public List<String> getChargerCities() {
        return chargerRepository.findDistinctPublicCities();
    }

    private Specification<Charger> chargerSpecification(
            String city,
            Long chargerTypeId,
            ChargingType chargingType,
            ChargerStatus status
    ) {
        return (root, query, criteriaBuilder) -> {
            root.fetch("chargerType", JoinType.INNER);
            query.distinct(true);

            Join<Charger, ChargerType> chargerType = root.join("chargerType", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isTrue(root.get("active")));
            predicates.add(criteriaBuilder.isTrue(chargerType.get("active")));

            if (city != null && !city.isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("city")),
                        city.trim().toLowerCase()
                ));
            }
            if (chargerTypeId != null) {
                predicates.add(criteriaBuilder.equal(chargerType.get("id"), chargerTypeId));
            }
            if (chargingType != null) {
                predicates.add(criteriaBuilder.equal(root.get("chargingType"), chargingType));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private ChargingType parseChargingType(String chargingType) {
        if (chargingType == null || chargingType.isBlank()) {
            return null;
        }
        try {
            return ChargingType.valueOf(chargingType.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(BAD_REQUEST, "Invalid charging type");
        }
    }

    private ChargerStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return ChargerStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(BAD_REQUEST, "Invalid charger status");
        }
    }

    private Sort chargerSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return defaultChargerSort();
        }
        return switch (sort) {
            case "nameAsc" -> Sort.by(Sort.Order.asc("name"), Sort.Order.asc("id"));
            case "cityAsc" -> Sort.by(Sort.Order.asc("city"), Sort.Order.asc("id"));
            case "powerDesc" -> Sort.by(Sort.Order.desc("powerKw"), Sort.Order.asc("id"));
            case "powerAsc" -> Sort.by(Sort.Order.asc("powerKw"), Sort.Order.asc("id"));
            default -> defaultChargerSort();
        };
    }

    private Sort defaultChargerSort() {
        return Sort.by(Sort.Order.asc("displayOrder"), Sort.Order.asc("id"));
    }
}
