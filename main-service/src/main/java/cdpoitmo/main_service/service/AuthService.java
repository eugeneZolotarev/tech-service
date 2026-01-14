package cdpoitmo.main_service.service;

import cdpoitmo.main_service.dto.authDTO.LoginDTO;
import cdpoitmo.main_service.dto.authDTO.RegistrationDTO;
import cdpoitmo.main_service.dto.authDTO.TokenDTO;

public interface AuthService {
    void register(RegistrationDTO registrationRequest);
    TokenDTO login(LoginDTO requestLogin);
    TokenDTO refreshToken(String refreshToken);
}
