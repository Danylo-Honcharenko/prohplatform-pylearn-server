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
        User user = userDao.findByEmail(username)
                .getFirst();
        // Перевіряємо що користувач присутній
        if (user == null) throw new UsernameNotFoundException("User not found!");
        // Намагаємось знайти роль
        Role role = roleDao.getById(user.getRoleId());
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
