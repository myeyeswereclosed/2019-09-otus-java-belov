package ru.otus.hw15.messagesystem.handler.front.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw15.common.Serializer;
import ru.otus.hw15.domain.User;
import ru.otus.hw15.messagesystem.handler.front.FrontendService;
import ru.otus.hw15.messagesystem.Message;
import ru.otus.hw15.messagesystem.handler.MessageHandler;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserDataResponseHandler implements MessageHandler {
  private static final Logger logger = LoggerFactory.getLogger(UserDataResponseHandler.class);

  private final FrontendService frontendService;
  private final Serializer serializer;

  @Autowired
  public UserDataResponseHandler(
      FrontendService frontendService,
      Serializer serializer
  ) {
    this.frontendService = frontendService;
    this.serializer = serializer;
  }

  @Override
  public Optional<Message> handle(Message msg) {
    logger.info("New message: {}", msg);

    try {
      UUID sourceMessageId =
          msg.getSourceMessageId()
              .orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));

      frontendService
          .takeConsumer(sourceMessageId, User.class)
          .ifPresent(consumer -> consumer.accept(makeUser(msg)));
    } catch (Exception ex) {
      logger.error("Erroneous message: " + msg, ex);
    }

    return Optional.empty();
  }

  private User makeUser(Message message) {
    return serializer.deserialize(message.getPayload(), User.class);
  }
}
