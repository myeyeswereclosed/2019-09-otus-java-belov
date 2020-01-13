package ru.otus.hw12.api.dao;

import ru.otus.hw12.api.sessionmanager.SessionManager;
import ru.otus.hw12.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    Optional<User> findRandomUser();

    Optional<User> findByLogin(String login);

    Optional<User> save(User user);

    List<User> findAll();

    default Optional<SessionManager> sessionManager() {
        return Optional.empty();
    }
}