package ru.otus.hw16.frontend_client.service.user;

import org.springframework.stereotype.Service;
import ru.otus.hw16.frontend_client.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
  Optional<User> save(User user);

  List<User> allUsers();

  List<User> clients();

  Optional<User> getByCredentials(String login, String password);
}
