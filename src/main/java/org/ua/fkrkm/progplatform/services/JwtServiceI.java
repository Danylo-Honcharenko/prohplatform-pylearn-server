package org.ua.fkrkm.progplatform.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.ua.fkrkm.progplatform.dto.TokenInfo;

import java.util.UUID;

/**
 * Інтерфейс для роботи з JWT токенами
 */
public interface JwtServiceI {
    /**
     * Згенерувати токен
     *
     * @param userId ID користувача
     * @param email email користувача
     * @param sid ID сесії
     * @return TokenInfo інформація про токен
     */
    TokenInfo generateToken(Long userId, String email, UUID sid);
    /**
     * Перевірити валідність токена
     *
     * @param token токен
     * @param user користувач
     * @return boolean true/false
     */
    boolean isTokenValid(String token, UserDetails user);
    /**
     * Отримати ім'я користувача з токена
     *
     * @param token токен
     * @return String ім'я користувача
     */
    String extractUserName(String token);
    /**
     * Отримати ID користувача з токена
     *
     * @param token токен
     * @return Long ID користувача
     */
    Long extractUserId(String token);
    /**
     * Отримати ID сесії з токена
     *
     * @param token токен
     * @return String ID сесії
     */
    String extractSid(String token);
    /**
     * Отримати час дії токена
     *
     * @return Long час дії токена
     */
    Long getExpirationTime();
}
