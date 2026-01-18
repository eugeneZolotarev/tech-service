package cdpoitmo.main_service.dto.bookingDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record RevenueDTO (
    LocalDate date,
    BigDecimal revenue
) {
}
