package ru.otus.hw12.services.auth;

import ru.otus.hw12.model.User;
import java.util.Optional;

public interface UserAuthService {
    Optional<User> authenticated(String login, String password);
}
