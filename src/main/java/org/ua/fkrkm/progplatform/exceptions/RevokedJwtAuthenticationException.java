package org.ua.fkrkm.progplatform.exceptions;

import io.jsonwebtoken.JwtException;

public class RevokedJwtAuthenticationException extends JwtException {
    public RevokedJwtAuthenticationException(String message) {
        super(message);
    }
}
