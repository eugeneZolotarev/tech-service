package cdpoitmo.main_service.service;

import cdpoitmo.main_service.dto.TechServiceRequestDTO;
import cdpoitmo.main_service.dto.TechServiceResponseDTO;

import java.util.List;

public interface TechService {
    Long createNewService(TechServiceRequestDTO techServiceRequestDTO);

    Long updateService(TechServiceRequestDTO techServiceRequestDTO);

    List<TechServiceResponseDTO> getAllServices();

    TechServiceResponseDTO getSericeById(long id);
}
