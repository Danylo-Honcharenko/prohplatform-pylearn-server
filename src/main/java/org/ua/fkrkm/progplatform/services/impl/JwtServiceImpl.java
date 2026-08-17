package org.ua.fkrkm.progplatform.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.ua.fkrkm.progplatform.dto.GeneratedToken;
import org.ua.fkrkm.progplatform.services.JwtServiceI;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtServiceI {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Override
    public GeneratedToken generateToken(String email) {
        return this.buildToken(new HashMap<>(), email, jwtExpiration);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails user) {
        String userName = this.extractUserName(token);
        return (userName.equals(user.getUsername())
                && !isTokenExpired(token));
    }

    @Override
    public String extractUserName(String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    @Override
    public Long getExpirationTime() {
        return this.jwtExpiration;
    }

    private GeneratedToken buildToken(Map<String, Object> extraClaims, String email, long expiration) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        String token = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        return new GeneratedToken(token, expirationDate);
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
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] decode = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(decode);
    }
}
