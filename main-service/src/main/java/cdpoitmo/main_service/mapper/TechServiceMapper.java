package cdpoitmo.main_service.mapper;

import cdpoitmo.main_service.dto.techServiceDTO.TechServiceRequestDTO;
import cdpoitmo.main_service.dto.techServiceDTO.TechServiceResponseDTO;
import cdpoitmo.main_service.entity.TechServiceEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TechServiceMapper {

    TechServiceResponseDTO toDTO(TechServiceEntity techServiceEntity);

    List<TechServiceResponseDTO> toDTO(List<TechServiceEntity> techServiceEntities);

    TechServiceEntity toEntity(TechServiceRequestDTO techServiceRequestDTO);

    void updateEntity(TechServiceRequestDTO techServiceRequestDTO, @MappingTarget TechServiceEntity techServiceEntity);
}
