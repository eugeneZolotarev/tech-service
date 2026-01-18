package cdpoitmo.main_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingStatisticDTO (
        Long bookingId,
        Long userId,
        String serviceName,
        BigDecimal priceWithDiscount,
        LocalDateTime eventDate
) {
}
