package cdpoitmo.main_service.dto.adminDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOperatorRequestDTO(
        @NotBlank(message = "username обязателен")
        String username,

        @NotBlank(message = "password обязателен")
        @Size(min = 6, message = "Пароль должен содержать не менее 6 символов")
        String password,

        @NotBlank(message = "email обязателен")
        @Email(message = "Некорректная запись адреса почты")
        String email
) {
}
