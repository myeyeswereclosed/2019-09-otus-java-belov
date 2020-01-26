package ru.otus.hw13.repository;

import ru.otus.hw13.api.sessionmanager.SessionManager;
import ru.otus.hw13.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByLogin(String login);

    Optional<User> save(User user);

    List<User> findAll();

    SessionManager sessionManager();
}