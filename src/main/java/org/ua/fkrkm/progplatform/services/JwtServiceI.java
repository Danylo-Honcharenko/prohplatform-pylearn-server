package org.ua.fkrkm.progplatform.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.ua.fkrkm.progplatform.dto.GeneratedToken;

import java.util.Map;

public interface JwtServiceI {
    GeneratedToken generateToken(UserDetails user);
    boolean isTokenValid(String token, UserDetails user);
    String extractUserName(String token);
    Long getExpirationTime();
}
