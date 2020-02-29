package ru.otus.hw15.messagesystem.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.hw15.common.Serializer;
import ru.otus.hw15.domain.User;
import ru.otus.hw15.messagesystem.Message;
import ru.otus.hw15.messagesystem.MessageType;
import ru.otus.hw15.service.user.UserService;
import java.util.Optional;

@Component
public class UserDataRequestHandler implements MessageHandler {
  private final UserService userService;
  private final Serializer serializer;

  @Autowired
  public UserDataRequestHandler(
      @Qualifier("commonUserService") UserService userService,
      Serializer serializer
  ) {
    this.userService = userService;
    this.serializer = serializer;
  }

  @Override
  public Optional<Message> handle(Message msg) {
    User user = serializer.deserialize(msg.getPayload(), User.class);

    return
        userService.save(user).map(
            data -> new Message(
                msg.getTo(),
                msg.getFrom(),
                msg.getSourceMessageId().get(),
                MessageType.NEW_USER.getValue(),
                serializer.serialize(data)
            )
        );
  }
}
