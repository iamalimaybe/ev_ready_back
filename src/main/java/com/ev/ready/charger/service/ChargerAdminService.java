package com.ev.ready.charger.service;

import com.ev.ready.catalog.service.ChargerTypeReadService;
import com.ev.ready.charger.domain.Charger;
import com.ev.ready.charger.dto.AdminChargerResponse;
import com.ev.ready.charger.dto.UpdateChargerRequest;
import com.ev.ready.charger.enums.ChargerStatus;
import com.ev.ready.charger.repository.ChargerRepository;
import com.ev.ready.common.PageResponse;
import com.ev.ready.common.enums.VerificationStatus;
import com.ev.ready.common.util.StringUtil;
import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ChargerAdminService {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final ChargerRepository chargerRepository;
    private final ChargerTypeReadService chargerTypeReadService;

    public ChargerAdminService(
            ChargerRepository chargerRepository,
            ChargerTypeReadService chargerTypeReadService
    ) {
        this.chargerRepository = chargerRepository;
        this.chargerTypeReadService = chargerTypeReadService;
    }

    @Transactional(readOnly = true)
    public PageResponse<AdminChargerResponse> getAdminChargers(
            Integer page,
            Integer size,
            Boolean active,
            String city,
            ChargerStatus status,
            VerificationStatus verificationStatus
    ) {
        return PageResponse.from(chargerRepository.findAll(
                chargerSpecification(active, city, status, verificationStatus),
                pageRequest(page, size)
        ).map(AdminChargerResponse::from));
    }

    @Transactional(readOnly = true)
    public AdminChargerResponse getAdminCharger(Long id) {
        return chargerRepository.findById(id)
                .map(AdminChargerResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Charger not found."
                ));
    }

    @Transactional
    public AdminChargerResponse updateAdminCharger(
            Long id,
            UpdateChargerRequest request,
            String updatedBy
    ) {
        Charger charger = chargerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Charger not found."
                ));

        charger.setChargerType(chargerTypeReadService.getActiveChargerType(request.chargerTypeId()));
        charger.setName(requiredText(request.name(), "Name is required."));
        charger.setCity(requiredText(request.city(), "City is required."));
        charger.setArea(StringUtil.trimToNull(request.area()));
        charger.setAddress(StringUtil.trimToNull(request.address()));
        charger.setLatitude(request.latitude());
        charger.setLongitude(request.longitude());
        charger.setChargingType(request.chargingType());
        charger.setStatus(request.status());
        charger.setPowerKw(request.powerKw());
        charger.setPriceNote(StringUtil.trimToNull(request.priceNote()));
        charger.setDescription(requiredText(request.description(), "Description is required."));
        charger.setImage(StringUtil.trimToNull(request.image()));
        charger.setSourceUrl(StringUtil.trimToNull(request.sourceUrl()));
        charger.setSourceLabel(StringUtil.trimToNull(request.sourceLabel()));
        charger.setSourceCheckedAt(request.sourceCheckedAt());
        charger.setVerificationStatus(request.verificationStatus());
        charger.setActive(request.active());
        charger.setDisplayOrder(request.displayOrder());

        charger.setUpdatedAt(OffsetDateTime.now());
        charger.setUpdatedBy(StringUtil.trimToNull(updatedBy));

        return AdminChargerResponse.from(charger);
    }

    private Specification<Charger> chargerSpecification(
            Boolean active,
            String city,
            ChargerStatus status,
            VerificationStatus verificationStatus
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (active != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), active));
            }
            if (city != null && !city.isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("city")),
                        city.trim().toLowerCase()
                ));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
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

    private String requiredText(String value, String message) {
        String trimmedValue = StringUtil.trimToNull(value);
        if (trimmedValue == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        return trimmedValue;
    }
}
