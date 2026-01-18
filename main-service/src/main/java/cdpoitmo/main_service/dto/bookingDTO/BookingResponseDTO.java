package cdpoitmo.main_service.dto.bookingDTO;

import cdpoitmo.main_service.constants.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingResponseDTO (
        Long id,
        String serviceName,
        LocalDateTime appointmentTime,
        BigDecimal finalPrice,
        Double discountPercent,
        BigDecimal originalPrice,
        BookingStatus status
){
}
