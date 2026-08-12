package org.ua.fkrkm.progplatform.exceptions;

import io.jsonwebtoken.JwtException;

public class InvalidJwtAuthenticationException extends JwtException {
    public InvalidJwtAuthenticationException(String message) {
        super(message);
    }
}
