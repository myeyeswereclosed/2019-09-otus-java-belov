package ru.otus.hw16.frontend_client.service;

import ru.otus.hw16.lib.domain.User;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface UserService {
  void save(User user, Consumer<User> consumer);

  List<User> allUsers();

  List<User> clients();

  Optional<User> getByCredentials(String login, String password);
}
