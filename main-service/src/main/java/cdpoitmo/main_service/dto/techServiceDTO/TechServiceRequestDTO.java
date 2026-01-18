package cdpoitmo.main_service.dto.techServiceDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TechServiceRequestDTO (
        @NotBlank(message = "Название услуги не может быть пустым и не должно содержать пробел")
        @Size(min = 3, max = 100, message = "Название услуги должно быть от 3 до 100 символов")
        String name,

        @NotBlank(message = "Описание услуги не может быть пустым и не должно содержать пробел")
        @Size(max = 1000, message = "Описание услуги не должно превышать 1000 символов")
        String description,

        @NotNull(message = "Цена услуги обязательна")
        @Positive(message = "Цена услуги должна быть положительным числом")
        BigDecimal price
) {
}
