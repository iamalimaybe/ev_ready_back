package com.ev.ready.catalog.service;

import com.ev.ready.catalog.domain.ChargerType;
import com.ev.ready.catalog.dto.ChargerTypeResponse;
import com.ev.ready.catalog.repository.ChargerTypeRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ChargerTypeReadService {

    private final ChargerTypeRepository chargerTypeRepository;

    public ChargerTypeReadService(ChargerTypeRepository chargerTypeRepository) {
        this.chargerTypeRepository = chargerTypeRepository;
    }

    public List<ChargerTypeResponse> getChargerTypes() {
        return chargerTypeRepository.findByActiveTrueOrderByDisplayOrderAscNameAsc()
                .stream()
                .map(ChargerTypeResponse::from)
                .toList();
    }

    public ChargerType getActiveChargerType(Long id) {
        return chargerTypeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Charger type must exist and be active."
                ));
    }
}
