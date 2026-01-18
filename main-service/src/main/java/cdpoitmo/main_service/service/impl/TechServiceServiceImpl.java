package cdpoitmo.main_service.service.impl;

import cdpoitmo.main_service.dto.techServiceDTO.TechServiceRequestDTO;
import cdpoitmo.main_service.dto.techServiceDTO.TechServiceResponseDTO;
import cdpoitmo.main_service.entity.TechServiceEntity;
import cdpoitmo.main_service.exception.ResourceNotFoundException;
import cdpoitmo.main_service.mapper.TechServiceMapper;
import cdpoitmo.main_service.repository.TechServiceRepository;
import cdpoitmo.main_service.service.TechServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TechServiceServiceImpl implements TechServiceService {

    private final TechServiceRepository techServiceRepository;
    private final TechServiceMapper techServiceMapper;

    @Override
    public TechServiceResponseDTO getServiceById(long id) {
        TechServiceEntity result = techServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Услуга с таким id=" + id + " не существует"));
        return techServiceMapper.toDTO(result);
    }

    @Override
    public List<TechServiceResponseDTO> getAllServices() {
        List<TechServiceEntity> result = techServiceRepository.findAll();
        return techServiceMapper.toDTO(result);
    }

    @Override
    @Transactional
    public TechServiceResponseDTO createService(TechServiceRequestDTO techServiceRequestDTO) {
        TechServiceEntity techServiceEntity = techServiceMapper.toEntity(techServiceRequestDTO);
        TechServiceEntity result = techServiceRepository.save(techServiceEntity);
        return techServiceMapper.toDTO(result);
    }

    @Override
    @Transactional
    public TechServiceResponseDTO updateService(long id, TechServiceRequestDTO techServiceRequestDTO) {
        TechServiceEntity entity = techServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Услуга с таким id=" + id + " не существует"));
        techServiceMapper.updateEntity(techServiceRequestDTO, entity);
        TechServiceEntity result = techServiceRepository.save(entity);
        return techServiceMapper.toDTO(result);
    }
}
