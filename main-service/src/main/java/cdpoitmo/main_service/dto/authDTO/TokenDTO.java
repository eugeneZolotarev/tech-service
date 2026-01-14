package cdpoitmo.main_service.dto.authDTO;

public record TokenDTO (
        String accessToken,
        String refreshToken
) {
}
