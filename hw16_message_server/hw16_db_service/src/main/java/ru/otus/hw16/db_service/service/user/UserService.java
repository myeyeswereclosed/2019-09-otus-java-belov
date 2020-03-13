package ru.otus.hw16.db_service.service.user;

import ru.otus.hw16.lib.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
  Optional<User> save(User user);

  List<User> allUsers();

  List<User> clients();

  Optional<User> getByCredentials(String login, String password);
}
