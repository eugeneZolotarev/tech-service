package cdpoitmo.main_service.service.impl;

import cdpoitmo.main_service.service.JwtSecurityService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtSecurityServiceImpl implements JwtSecurityService {

    @Value("${security.jwtSecret}")
    private String jwtSecret;
    @Value("${security.jwtSecretExpiration}")
    private long jwtExpiration;
    @Value("${security.jwtRefreshSecret}")
    private String jwtRefreshSecret;
    @Value("${security.jwtRefreshExpiration}")
    private long jwtRefreshExpiration;

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities());
        return buildToken(claims, userDetails, jwtExpiration, jwtSecret);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtRefreshExpiration, jwtRefreshSecret);
    }

    @Override
    public String extractUsername(String token) {
        return extractUsername(token, jwtSecret);
    }

    @Override
    public String extractUsername(String token, String secret) {
        return extractClaim(token, Claims::getSubject, secret);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token, jwtSecret);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, jwtSecret);
    }

    @Override
    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token, jwtRefreshSecret);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, jwtRefreshSecret);
    }

    private String buildToken(Map<String, Object> claims,
                              UserDetails userDetails,
                              long expiration,
                              String secretString) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey(secretString))
                .compact();
    }

    private SecretKey getSecretKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String secret) {
        final Claims claims = extractAllClaims(token, secret);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, String secret) {
        return Jwts.parser()
                .verifyWith(getSecretKey(secret))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token, String secret) {
        return extractClaim(token, Claims::getExpiration, secret).before(new Date());
    }
}