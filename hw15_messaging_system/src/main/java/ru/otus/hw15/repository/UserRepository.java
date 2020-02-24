package ru.otus.hw15.repository;

import ru.otus.hw15.domain.User;
import ru.otus.hw15.sessionmanager.SessionManager;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByLoginAndPassword(String login, String password);

    Optional<User> save(User user);

    List<User> findAll();

    SessionManager sessionManager();
}