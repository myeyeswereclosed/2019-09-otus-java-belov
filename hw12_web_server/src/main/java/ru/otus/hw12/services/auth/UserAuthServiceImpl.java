package ru.otus.hw12.services.auth;

import org.slf4j.Logger;
import ru.otus.hw12.api.dao.UserDao;
import ru.otus.hw12.api.sessionmanager.SessionManager;
import ru.otus.hw12.model.User;
import ru.otus.hw12.services.hash.HashService;

import java.util.Optional;

public class UserAuthServiceImpl implements UserAuthService {
    private final UserDao userDao;
    private final Logger logger;

    public UserAuthServiceImpl(UserDao userDao, Logger logger) {
        this.userDao = userDao;
        this.logger = logger;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return
            userDao.findByLogin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false)
        ;
    }

    @Override
    public Optional<User> authenticated(String login, String password) {
        Optional<SessionManager> sessionManagerOptional = userDao.sessionManager();

        if (sessionManagerOptional.isEmpty()) {
            return Optional.empty();
        }

        try (SessionManager sessionManager = sessionManagerOptional.get()) {
            sessionManager.beginSession();

            try {
                Optional<User> userByLogin = userDao.findByLogin(login);
                sessionManager.commitSession();

                return
                    userByLogin
                        .filter(
                            user -> HashService.hash(password).equals(user.getPassword())
                        );
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                sessionManager.rollbackSession();
            }
        }

        return Optional.empty();
    }
}
