package org.ua.fkrkm.progplatform.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.ua.fkrkm.proglatformdao.dao.RoleDaoI;
import org.ua.fkrkm.proglatformdao.dao.UserDaoI;
import org.ua.fkrkm.proglatformdao.entity.Role;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.core.userdetails.User.*;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDaoI userDao;
    private final RoleDaoI roleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Отримуємо користувача
        List<User> users = userDao.findByEmail(username);
        // Перевіряємо що користувач присутній
        if (users.isEmpty()) throw new UsernameNotFoundException("User not found!");
        User user = users.getFirst();
        // Намагаємось знайти роль
        List<Role> roles = roleDao.getById(user.getRoleId());
        if (roles.isEmpty()) throw new ProgPlatformException(ErrorConsts.ROLE_NOT_FOUND);
        Role role = roles.getFirst();
        // Створюємо список ролів
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        // Повертаємо об'єкт з даними користувача
        return withUsername(username)
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
