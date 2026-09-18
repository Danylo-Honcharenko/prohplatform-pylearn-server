package org.ua.fkrkm.progplatform.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.ua.fkrkm.progplatform.dto.TokenInfo;

import java.util.UUID;

public interface JwtServiceI {
    TokenInfo generateToken(Long userId, String email, UUID sid);
    boolean isTokenValid(String token, UserDetails user);
    String extractUserName(String token);
    Long extractUserId(String token);
    String extractSid(String token);
    Long getExpirationTime();
}
