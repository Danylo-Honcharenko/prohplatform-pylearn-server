package org.ua.fkrkm.progplatform.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.ua.fkrkm.proglatformdao.dao.UserDaoI;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;
import org.ua.fkrkm.progplatform.services.AuthUserServiceI;

import java.util.Collection;
import java.util.List;

/**
 * Сервіс для роботи з поточним користувачем в системі
 */
@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserServiceI {

    // DAO для роботи з користувачами
    private final UserDaoI userDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public org.ua.fkrkm.proglatformdao.entity.User getCurrentAuthUser() {
        // Отримуємо поточний стан аутентифікації
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Отримуємо користувача
        User user = (User) authentication.getPrincipal();
        // Шукаємо повну інформацію в базі по Email
        List<org.ua.fkrkm.proglatformdao.entity.User> foundUsers = userDao.findByEmail(user.getUsername());
        // Перевіряємо що користувача знайдено
        if (foundUsers.isEmpty()) throw new ProgPlatformException(ErrorConsts.USER_NOT_FOUND);
        return foundUsers.getFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCurrentAuthUserAdmin() {
        // Отримуємо поточний стан аутентифікації
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Отримуємо користувача
        User user = (User) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCurrentAuthUserTeacher() {
        // Отримуємо поточний стан аутентифікації
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Отримуємо користувача
        User user = (User) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority("ROLE_TEACHER"));
    }
}
