package cdpoitmo.main_service.dto.adminDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DiscountRequestDTO (
        @NotNull(message = "Процент скидки обязателен")
        @Size(min = 0, max = 100, message = "Значение скидки задается в диапазоне от 0% до 100%")
        double discount
) {
}
