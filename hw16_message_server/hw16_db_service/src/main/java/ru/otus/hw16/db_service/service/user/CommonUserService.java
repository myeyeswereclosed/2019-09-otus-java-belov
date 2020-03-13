package ru.otus.hw16.db_service.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.hw16.lib.domain.User;
import ru.otus.hw16.db_service.repository.UserRepository;
import ru.otus.hw16.db_service.sessionmanager.SessionManager;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommonUserService implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(CommonUserService.class);
    private final UserRepository repository;

  @Autowired
  public CommonUserService(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<User> save(User user) {
      try (SessionManager sessionManager = repository.sessionManager()) {
          sessionManager.beginSession();

          try {
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

    @Override
    public List<User> clients() {
        return allUsers().stream().filter(User::isClient).collect(Collectors.toList());
    }

    @Override
    public Optional<User> getByCredentials(String login, String password) {
        if (Objects.isNull(login) || Objects.isNull(password)) {
            logger.warn("Login or password not found for user");

            return Optional.empty();
        }

        if (password.isEmpty()) {
            return Optional.empty();
        }

        try (SessionManager sessionManager = repository.sessionManager()) {
            sessionManager.beginSession();

            try {
                Optional<User> user = repository.findByLoginAndPassword(login, password);

                sessionManager.commitSession();

                user.ifPresentOrElse(
                    userStored -> logger.info("Found user: {}", userStored.toString()),
                    () -> logger.info("No user was found")
                );

                return user;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                sessionManager.rollbackSession();
            }
        }

      return Optional.empty();
    }
}
