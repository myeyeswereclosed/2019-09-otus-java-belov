package ru.otus.hw13.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.hw13.api.sessionmanager.SessionManager;
import ru.otus.hw13.domain.User;
import ru.otus.hw13.repository.UserRepository;
import ru.otus.hw13.service.hash.HashService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service("commonUserService")
public class CommonUserService implements UserService {
  private final UserRepository repository;
  private final HashService hashService;
  private final Logger logger;

  @Autowired
  public CommonUserService(UserRepository repository, HashService hashService, Logger logger) {
    this.repository = repository;
    this.hashService = hashService;
    this.logger = logger;
  }

  @Override
  public Optional<User> save(User user) {
      var hashedPassword = hashService.hash(user.getPassword());

      if (hashedPassword.isEmpty()) {
          return Optional.empty();
      }

      try (SessionManager sessionManager = repository.sessionManager()) {
          sessionManager.beginSession();

          try {
              user.setPassword(hashedPassword.get());
              Optional<User> userSaved = repository.save(user);
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
    try (SessionManager sessionManager = repository.sessionManager()) {
      sessionManager.beginSession();

      try {
        var all = repository.findAll();

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
