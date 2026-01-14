package cdpoitmo.main_service.dto.authDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginDTO (
    @NotBlank
    String username,
    @NotNull
    String password
) {}
