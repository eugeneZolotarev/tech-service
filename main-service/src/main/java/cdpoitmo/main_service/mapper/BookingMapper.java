package cdpoitmo.main_service.mapper;

import cdpoitmo.main_service.dto.bookingDTO.BookingResponseDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingTimeDTO;
import cdpoitmo.main_service.entity.Booking;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {

    @Mapping(target = "serviceName", source = "techServiceEntity.name")
    @Mapping(target = "originalPrice", source = "techServiceEntity.price")
    BookingResponseDTO toDTO(Booking booking);

    List<BookingResponseDTO> toDTO(List<Booking> bookings);

    void updateEntity(BookingTimeDTO bookingTimeDTO, @MappingTarget Booking booking);
}
