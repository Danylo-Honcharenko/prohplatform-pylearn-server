package org.ua.fkrkm.progplatform.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.ua.fkrkm.progplatform.dto.TokenInfo;
import org.ua.fkrkm.progplatform.services.JwtServiceI;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtServiceI {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Override
    public TokenInfo generateToken(Long userId, String email, UUID sid) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sid", sid.toString());
        claims.put("email", email);

        return this.buildToken(claims, userId, jwtExpiration);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails user) {
        String userName = this.extractUserName(token);
        return (userName.equals(user.getUsername())
                && !isTokenExpired(token));
    }

    @Override
    public String extractUserName(String token) {
        return this.extractClaim(token, (claims) -> claims.get("email", String.class));
    }

    @Override
    public Long extractUserId(String token) {
        String id = this.extractClaim(token, Claims::getSubject);
        return Long.valueOf(id);
    }

    @Override
    public String extractSid(String token) {
        return this.extractClaim(token, (claims) -> claims.get("sid", String.class));
    }

    @Override
    public Long getExpirationTime() {
        return this.jwtExpiration;
    }

    private TokenInfo buildToken(Map<String, Object> extraClaims, Long userId, long expiration) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        Date issuedAt = new Date(System.currentTimeMillis());

        String token = Jwts.builder()
                .subject(userId.toString())
                .claims(extraClaims)
                .issuedAt(issuedAt)
                .expiration(expirationDate)
                .signWith(getSignInKey())
                .compact();

        return new TokenInfo(token, (String) extraClaims.get("sid"), issuedAt, expirationDate);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaim(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
