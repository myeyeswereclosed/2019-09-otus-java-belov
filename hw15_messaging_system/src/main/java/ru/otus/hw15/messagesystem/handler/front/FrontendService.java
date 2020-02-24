package ru.otus.hw15.messagesystem.handler.front;

import ru.otus.hw15.domain.User;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendService {
  UUID getUserData(
      User user,
      Consumer<String> dataConsumer,
      String target,
      UUID identifier
  );

  void userSaved(UUID identifier, Consumer<User> dataConsumer);

  <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);
}

