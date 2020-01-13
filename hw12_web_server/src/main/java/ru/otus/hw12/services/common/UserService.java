package ru.otus.hw12.services.common;

import ru.otus.hw12.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
  Optional<User> save(User user);

  List<User> allUsers();
}
