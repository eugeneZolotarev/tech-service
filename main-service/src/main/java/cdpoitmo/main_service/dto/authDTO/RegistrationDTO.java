package cdpoitmo.main_service.dto.authDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrationDTO (
        @NotBlank
        String username,
        @NotNull
        String password,
        @Email
        String email
) {
}
