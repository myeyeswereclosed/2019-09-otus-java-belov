package ru.otus.hw10.api.dao;

import ru.otus.hw10.api.entity.User;
import ru.otus.hw10.api.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {
  Optional<User> findById(long id);

  long saveUser(User user);

  SessionManager getSessionManager();
}
