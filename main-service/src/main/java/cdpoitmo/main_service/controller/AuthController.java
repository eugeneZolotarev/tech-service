package cdpoitmo.main_service.controller;

import cdpoitmo.main_service.dto.authDTO.LoginDTO;
import cdpoitmo.main_service.dto.authDTO.RegistrationDTO;
import cdpoitmo.main_service.dto.authDTO.TokenDTO;
import cdpoitmo.main_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Validated
public class AuthController {

    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationDTO registrationDTO) {
        authService.register(registrationDTO);
        return ResponseEntity.ok("Пользователь зарегистрирован успешно");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        TokenDTO tokenDTO = authService.login(loginDTO);
        return ResponseEntity.ok(tokenDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDTO> refresh(@RequestBody String refreshToken) {
        TokenDTO tokenDTO = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(tokenDTO);
    }
}
