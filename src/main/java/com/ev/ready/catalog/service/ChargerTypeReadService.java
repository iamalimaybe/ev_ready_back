package com.ev.ready.catalog.service;

import com.ev.ready.catalog.dto.ChargerTypeResponse;
import com.ev.ready.catalog.repository.ChargerTypeRepository;
import java.util.List;
import org.springframework.stereotype.Service;

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
}
