package com.eventsitemanager.service;

import com.eventsitemanager.service.dto.GasStationIntegrationDTO;
import java.util.Optional;

public interface GasStationIntegrationService {
    GasStationIntegrationDTO save(GasStationIntegrationDTO gasStationIntegrationDTO);
    GasStationIntegrationDTO update(GasStationIntegrationDTO gasStationIntegrationDTO);
    Optional<GasStationIntegrationDTO> partialUpdate(GasStationIntegrationDTO gasStationIntegrationDTO);
    Optional<GasStationIntegrationDTO> findOne(Long id);
    void delete(Long id);
}
