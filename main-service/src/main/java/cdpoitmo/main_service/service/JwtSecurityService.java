package cdpoitmo.main_service.service;
import org.springframework.security.core.userdetails.UserDetails;


public interface JwtSecurityService {
    String generateToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String extractUsername(String token);

    String extractUsername(String token, String secret);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isRefreshTokenValid(String token, UserDetails userDetails);
}
