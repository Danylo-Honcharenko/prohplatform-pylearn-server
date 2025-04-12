package org.ua.fkrkm.progplatform.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.ua.fkrkm.progplatform.services.JwtServiceI;
import org.ua.fkrkm.progplatform.services.impl.JwtServiceImpl;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.core.userdetails.User.withUsername;

public class JwtServiceTest {

    private final JwtServiceI jwtService = new JwtServiceImpl();

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field secretKey = JwtServiceImpl.class.getDeclaredField("secretKey");
        secretKey.setAccessible(true);
        secretKey.set(jwtService, "23sdsd75lmj89U89ip90rtsAmd226pl90mm64dfdfkio78912Oios89mmty378Dsxbv");

        Field jwtExpiration = JwtServiceImpl.class.getDeclaredField("jwtExpiration");
        jwtExpiration.setAccessible(true);
        jwtExpiration.set(jwtService, 100000L);
    }

    @Test
    public void generateTokenTest() {
        User.UserBuilder buildUser = withUsername("test.test@gmail.com");
        buildUser.password("12345678");
        String token = jwtService.generateToken(buildUser.build());
        assertNotNull(token);
    }

    @Test
    public void extractTest() {
        String userEmail = "test.test@gmail.com";
        User.UserBuilder buildUser = withUsername(userEmail);
        buildUser.password("12345678");
        String token = jwtService.generateToken(buildUser.build());
        String userName = jwtService.extractUserName(token);
        assertEquals(userEmail, userName);
    }

    @Test
    public void isTokenValidTest() {
        String userEmail = "test.test@gmail.com";
        User.UserBuilder buildUser = withUsername(userEmail);
        buildUser.password("12345678");
        String token = jwtService.generateToken(buildUser.build());

        assertTrue(jwtService.isTokenValid(token, buildUser.build()));
    }

    @Test
    public void getExpirationTimeTest() {
        assertEquals(100000L, jwtService.getExpirationTime());
    }
}
