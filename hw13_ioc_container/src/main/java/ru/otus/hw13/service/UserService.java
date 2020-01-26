package ru.otus.hw13.service;

import ru.otus.hw13.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
  Optional<User> save(User user);

  List<User> allUsers();
}
