package org.ua.fkrkm.progplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * Об'ект згенерованого токену
 */
@Getter
@AllArgsConstructor
public class GeneratedToken {
    // Токен
    private String token;
    // Дата закінчення дії токену
    private Date expired;
}
