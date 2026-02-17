package org.ua.fkrkm.progplatform.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtServiceI {
    Map<String, String> generateToken(UserDetails user);
    boolean isTokenValid(String token, UserDetails user);
    String extractUserName(String token);
    Long getExpirationTime();
}
