package cdpoitmo.main_service.dto.techServiceDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TechServiceResponseDTO (
        Long id,
        String name,
        String description,
        BigDecimal price
) {
}
