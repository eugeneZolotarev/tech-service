package cdpoitmo.main_service.service;

import cdpoitmo.main_service.dto.techServiceDTO.TechServiceRequestDTO;
import cdpoitmo.main_service.dto.techServiceDTO.TechServiceResponseDTO;

import java.util.List;

public interface TechServiceService {
    TechServiceResponseDTO getServiceById(long id);
    List<TechServiceResponseDTO> getAllServices();
    TechServiceResponseDTO createService(TechServiceRequestDTO techServiceRequestDTO);
    TechServiceResponseDTO updateService(long id, TechServiceRequestDTO techServiceRequestDTO);
}
