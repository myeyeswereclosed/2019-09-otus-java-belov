package ru.otus.hw12.services.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw12.api.dao.UserDao;
import ru.otus.hw12.api.sessionmanager.SessionManager;
import ru.otus.hw12.model.User;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CommonUserService implements UserService {
  private final UserDao userDao;
  private final Logger logger;

  public CommonUserService(UserDao userDao, Logger logger) {
    this.userDao = userDao;
    this.logger = logger;
  }

  @Override
  public Optional<User> save(User user) {
    Optional<SessionManager> sessionManagerOptional = userDao.sessionManager();

    if (sessionManagerOptional.isEmpty()) {
      return Optional.empty();
    }

    try (SessionManager sessionManager = sessionManagerOptional.get()) {
      sessionManager.beginSession();

      try {
        Optional<User> userSaved = userDao.save(user);
        sessionManager.commitSession();

        logger.info("Saved user: {}", userSaved);

        return userSaved;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);

        sessionManager.rollbackSession();
      }
    }

    return Optional.empty();
  }

  @Override
  public List<User> allUsers() {
    Optional<SessionManager> sessionManagerOptional = userDao.sessionManager();

    if (sessionManagerOptional.isEmpty()) {
      return Collections.emptyList();
    }

    try (SessionManager sessionManager = sessionManagerOptional.get()) {
      sessionManager.beginSession();

      try {
        var all = userDao.findAll();

        sessionManager.commitSession();

        return all;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);

        sessionManager.rollbackSession();
      }
    }

    return Collections.emptyList();
  }
}
