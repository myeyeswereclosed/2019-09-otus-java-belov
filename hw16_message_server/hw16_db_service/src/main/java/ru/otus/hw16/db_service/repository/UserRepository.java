package ru.otus.hw16.db_service.repository;

import ru.otus.hw16.lib.domain.User;
import ru.otus.hw16.db_service.sessionmanager.SessionManager;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByLoginAndPassword(String login, String password);

    Optional<User> save(User user);

    List<User> findAll();

    SessionManager sessionManager();
}