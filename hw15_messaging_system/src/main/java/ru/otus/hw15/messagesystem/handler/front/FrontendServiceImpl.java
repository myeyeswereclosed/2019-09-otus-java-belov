package ru.otus.hw15.messagesystem.handler.front;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.hw15.domain.User;
import ru.otus.hw15.messagesystem.Message;
import ru.otus.hw15.messagesystem.MessageType;
import ru.otus.hw15.messagesystem.client.FrontendClient;
import ru.otus.hw15.messagesystem.client.MsClient;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Component
public class FrontendServiceImpl implements FrontendService {
  private static final Logger logger = LoggerFactory.getLogger(FrontendServiceImpl.class);

  private final Map<UUID, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
  private final MsClient msClient;

  @Autowired
  public FrontendServiceImpl(FrontendClient msClient) {
    this.msClient = msClient;
  }

  @Override
  public UUID getUserData(User user, Consumer<String> dataConsumer, String target, UUID identifier) {
    Message outMsg = msClient.produceMessage(target, user, MessageType.NEW_USER, identifier);

    consumerMap.put(outMsg.getId(), dataConsumer);

    msClient.sendMessage(outMsg);

    return outMsg.getSourceMessageId().get();
  }

  @Override
  public void userSaved(UUID identifier, Consumer<User> dataConsumer) {
    consumerMap.put(identifier, dataConsumer);
  }

  @Override
  public <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass) {
    Consumer<T> consumer = (Consumer<T>) consumerMap.remove(sourceMessageId);

    if (consumer == null) {
      logger.warn("consumer not found for: {}", sourceMessageId);
      return Optional.empty();
    }

    return Optional.of(consumer);
  }
}
