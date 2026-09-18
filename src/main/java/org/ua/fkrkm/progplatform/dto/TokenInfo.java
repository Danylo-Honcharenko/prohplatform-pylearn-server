package org.ua.fkrkm.progplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

/**
 * Об'ект згенерованого токену
 */
@Getter
@AllArgsConstructor
public class TokenInfo {
    // Токен
    private String token;
    // ID сессії
    private String sid;
    // Дата створення токена
    private Date created;
    // Дата закінчення дії токена
    private Date expired;
}
