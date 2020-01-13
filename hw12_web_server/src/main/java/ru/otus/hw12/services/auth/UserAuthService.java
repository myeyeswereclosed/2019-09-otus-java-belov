package ru.otus.hw12.services.auth;

import ru.otus.hw12.model.User;

import java.util.Optional;

public interface UserAuthService {
    boolean authenticate(String login, String password);

    Optional<User> authenticated(String login, String password);
}
