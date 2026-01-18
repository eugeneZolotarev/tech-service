package cdpoitmo.main_service.dto.adminDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DiscountRequestDTO (
        @NotNull(message = "Процент скидки обязателен")
        @Min(value = 0, message = "Минимальное значение равно 0")
        @Max(value = 100, message = "Максимальное значение равно 100")
        double discount
) {
}
