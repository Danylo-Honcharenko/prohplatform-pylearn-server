package org.ua.fkrkm.progplatform.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.ua.fkrkm.proglatformdao.dao.AuthDaoI;

@Component
@RequiredArgsConstructor
public class DeletingExpiredSessionsInDB {
    // DAO для роботи з аутентифікованими користувачами
    private final AuthDaoI authDao;

    @Scheduled(fixedRate = 5000)
    public void run() {
        this.authDao.deleteExpiredSessions();
    }
}
