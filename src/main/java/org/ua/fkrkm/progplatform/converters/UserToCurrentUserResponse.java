package org.ua.fkrkm.progplatform.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.dao.RoleDaoI;
import org.ua.fkrkm.proglatformdao.dao.TestResultDaoI;
import org.ua.fkrkm.proglatformdao.entity.Role;
import org.ua.fkrkm.proglatformdao.entity.TestResult;
import org.ua.fkrkm.proglatformdao.entity.User;
import org.ua.fkrkm.progplatformclientlib.response.*;
import org.ua.fkrkm.progplatform.exceptions.ErrorConsts;
import org.ua.fkrkm.progplatform.exceptions.ProgPlatformException;

@Component
@RequiredArgsConstructor
public class UserToCurrentUserResponse implements Converter<User, CurrentUserResponse> {

    // DAO для роботи з ролями
    private final RoleDaoI roleDao;
    // DAO для роботи з результатами тестування
    private final TestResultDaoI resultDao;

    @Override
    public CurrentUserResponse convert(User source) {
        int level = this.getLevelFromAllTestResultsByUserId(source.getId());
        String levelAlias = this.getLevelAliasByLevel(level);
        return CurrentUserResponse.builder()
                .id(source.getId())
                .firstName(source.getFirst_name())
                .lastName(source.getLast_name())
                .email(source.getEmail())
                .level(level)
                .levelAlias(levelAlias)
                .role(this.getRoleNameById(source.getRoleId()))
                .build();
    }

    /**
     * Отримати ім'я ролі по ID
     *
     * @param id ID ролі
     * @return String ім'я ролі
     */
    private String getRoleNameById(int id) {
        try {
            // Отримуємо роль по ID
            Role role = roleDao.getById(id);
            return role.getName();
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ProgPlatformException(ErrorConsts.ROLE_NOT_FOUND);
        }
    }

    /**
     * Отримати рівень користувача за результатами тестування
     *
     * @param userId ID користувача
     * @return int рівень
     */
    private int getLevelFromAllTestResultsByUserId(int userId) {
        return resultDao.getTestResultsByUserId(userId)
                .stream()
                .map(TestResult::getAssessment)
                .reduce(Integer::sum)
                .orElse(0);
    }

    /**
     * Отримати псевдонім рівня
     *
     * @param level рівень
     * @return String псевдонім рівня
     */
    private String getLevelAliasByLevel(int level) {
        if (level >= 10 && level < 20) {
            return "Програміст-любитель";
        } else if (level >= 20) {
            return "Професіонал";
        } else {
            return "Новачок";
        }
    }
}
