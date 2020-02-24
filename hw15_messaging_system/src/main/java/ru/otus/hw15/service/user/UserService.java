package ru.otus.hw15.service.user;

import ru.otus.hw15.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
  Optional<User> save(User user);

  List<User> allUsers();

  List<User> clients();

  Optional<User> getByCredentials(String login, String password);
}
