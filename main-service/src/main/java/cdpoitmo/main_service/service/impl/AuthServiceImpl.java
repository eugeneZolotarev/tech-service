package cdpoitmo.main_service.service.impl;

import cdpoitmo.main_service.constants.UserRole;
import cdpoitmo.main_service.dto.authDTO.LoginDTO;
import cdpoitmo.main_service.dto.authDTO.RegistrationDTO;
import cdpoitmo.main_service.dto.authDTO.TokenDTO;
import cdpoitmo.main_service.entity.ApplicationUser;
import cdpoitmo.main_service.entity.Token;
import cdpoitmo.main_service.exception.BusinessValidationException;
import cdpoitmo.main_service.exception.ResourceNotFoundException;
import cdpoitmo.main_service.exception.UserAlreadyExistsException;
import cdpoitmo.main_service.repository.TokenRepository;
import cdpoitmo.main_service.repository.UserRepository;
import cdpoitmo.main_service.service.AuthService;
import cdpoitmo.main_service.service.JwtSecurityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailService userDetailService;
    private final JwtSecurityService jwtSecurityService;
    private final TokenRepository tokenRepository;

    @Value("${security.jwtRefreshSecret}")
    private String jwtRefreshSecret;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           UserDetailService userDetailService,
                           JwtSecurityService jwtSecurityService,
                           TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        this.jwtSecurityService = jwtSecurityService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public void register(RegistrationDTO registrationRequest) {
        if (userRepository.findByUsername(registrationRequest.username()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с таким имененем уже существует");
        }

        ApplicationUser newUser = ApplicationUser.builder()
                        .username(registrationRequest.username())
                                .password(passwordEncoder.encode(registrationRequest.password()))
                                        .email(registrationRequest.email())
                                                .userRole(UserRole.USER)
                                                        .build();

        userRepository.save(newUser);
    }

    @Override
    @Transactional
    public TokenDTO login(LoginDTO requestLogin) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestLogin.username(), requestLogin.password())
        );

        ApplicationUser neededUser = userRepository.findByUsername(requestLogin.username()).orElseThrow(
                () -> new ResourceNotFoundException("Пользователь с таким именем не был найден")
        );

        UserDetails userDetails = userDetailService.loadUserByUsername(requestLogin.username());

        String accessToken = jwtSecurityService.generateToken(userDetails);
        String refreshToken = jwtSecurityService.generateRefreshToken(userDetails);

        revokeAllUserToken(neededUser);
        saveUserToken(neededUser, refreshToken);

        return new TokenDTO(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public TokenDTO refreshToken(String refreshToken) {
        String username = jwtSecurityService.extractUsername(refreshToken, jwtRefreshSecret);

        ApplicationUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с таким именем не был найден")
                );

        boolean isTokenValid = tokenRepository.findByRefreshToken(refreshToken)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);

        if (jwtSecurityService.isRefreshTokenValid(refreshToken, userDetailService.loadUserByUsername(appUser.getUsername())) && isTokenValid) {
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            String accessToken = jwtSecurityService.generateToken(userDetails);

            return new TokenDTO(accessToken, refreshToken);
        } else {
            throw new BusinessValidationException("Refresh токен невалиден или отозван");
        }
    }

    private void revokeAllUserToken(ApplicationUser user){
        List<Token> usersTokens = tokenRepository.findAllValidTokensByApplicationUserId(user.getId());

        if (usersTokens.isEmpty()) return;

        usersTokens.forEach((token) ->
        {
            token.setRevoked(true);
            token.setExpired(true);
        });

        tokenRepository.saveAll(usersTokens);
    }

    private void saveUserToken(ApplicationUser needeedUser, String refreshToken) {
        Token token = Token.builder()
                .refreshToken(refreshToken)
                .applicationUser(needeedUser)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

}
