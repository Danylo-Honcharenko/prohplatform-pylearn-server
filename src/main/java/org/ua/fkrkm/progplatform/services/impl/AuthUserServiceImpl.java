package org.ua.fkrkm.progplatform.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.ua.fkrkm.proglatformdao.dao.UserDaoI;
import org.ua.fkrkm.progplatform.consts.Roles;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;
import org.ua.fkrkm.progplatform.services.AuthUserServiceI;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Сервіс для роботи з поточним користувачем у системі
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
        // Перевіряємо, що отримали
        if (Objects.isNull(authentication)) throw new ProgPlatformException("Not get authentication!");
        // Отримуємо користувача
        User user = (User) authentication.getPrincipal();
        // Перевіряємо, що отримали користувача
        if (Objects.isNull(user)) throw new ProgPlatformException("Not get user!");
        // Шукаємо повну інформацію в базі по Email
        List<org.ua.fkrkm.proglatformdao.entity.User> foundUsers = userDao.findByEmail(user.getUsername());
        // Перевіряємо, що користувача знайдено
        if (foundUsers.isEmpty()) throw new ProgPlatformException(ErrorConsts.USER_NOT_FOUND);
        return foundUsers.getFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCurrentAuthUserAdmin() {
        Collection<? extends GrantedAuthority> grantedAuthorities = this.getGrantedAuthorities();
        return grantedAuthorities.contains(new SimpleGrantedAuthority(Roles.ADMIN.getRoleName()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCurrentAuthUserTeacher() {
        Collection<? extends GrantedAuthority> grantedAuthorities = this.getGrantedAuthorities();
        return grantedAuthorities.contains(new SimpleGrantedAuthority(Roles.TEACHER.getRoleName()));
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities() {
        // Отримуємо поточний стан аутентифікації
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Перевіряємо, що отримали
        if (Objects.isNull(authentication)) throw new ProgPlatformException("Not get authentication!");
        // Отримуємо користувача
        User user = (User) authentication.getPrincipal();
        // Перевіряємо, що отримали користувача
        if (Objects.isNull(user)) throw new ProgPlatformException("Not get user!");
        return user.getAuthorities();
    }
}
