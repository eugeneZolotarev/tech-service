package cdpoitmo.main_service.controller;

import cdpoitmo.main_service.dto.authDTO.LoginDTO;
import cdpoitmo.main_service.dto.authDTO.RegistrationDTO;
import cdpoitmo.main_service.dto.authDTO.TokenDTO;
import cdpoitmo.main_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Validated
@Tag(name = "Аутентификация и регистрация", description = "Методы для управления сессиями пользователей, регистрации новых аккаунтов и обновления токенов доступа.")
public class AuthController {

    private AuthService authService;

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создает новый аккаунт в системе с ролью 'USER'. После успешной регистрации пользователь может авторизоваться и получить доступ к бронированию услуг."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные (неверный формат email или короткий пароль)"),
            @ApiResponse(responseCode = "409", description = "Конфликт: пользователь с таким username или email уже существует")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationDTO registrationDTO) {
        authService.register(registrationDTO);
        return ResponseEntity.ok("Пользователь зарегистрирован успешно");
    }

    @Operation(
            summary = "Авторизация пользователя",
            description = "Принимает логин и пароль. В случае успеха возвращает JWT access-токен (короткоживущий) и refresh-токен для обновления сессии."
    )
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        TokenDTO tokenDTO = authService.login(loginDTO);
        return ResponseEntity.ok(tokenDTO);
    }

    @Operation(
            summary = "Обновление токена доступа",
            description = "Позволяет получить новый accessToken без повторного ввода логина и пароля. Используйте полученный ранее refreshToken. Refresh-токен обновляется раз в день."
    )
    @PostMapping("/refresh")
    public ResponseEntity<TokenDTO> refresh(@RequestBody String refreshToken) {
        TokenDTO tokenDTO = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(tokenDTO);
    }
}
