package cdpoitmo.dwh_service.DTO;

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
