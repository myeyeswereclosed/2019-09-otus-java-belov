package ru.otus.hw15.messagesystem.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw15.common.Serializer;
import ru.otus.hw15.messagesystem.Message;
import ru.otus.hw15.messagesystem.MessageSystem;
import ru.otus.hw15.messagesystem.MessageType;
import ru.otus.hw15.messagesystem.handler.MessageHandler;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

abstract public class MsClientImpl implements MsClient {
  private static final Logger logger = LoggerFactory.getLogger(MsClientImpl.class);

  private final MessageSystem messageSystem;
  private final Map<String, MessageHandler> handlers = new ConcurrentHashMap<>();

  private final Serializer serializer;

  public MsClientImpl(MessageSystem messageSystem, Serializer serializer) {
    this.messageSystem = messageSystem;

    this.serializer = serializer;
  }

  @Override
  public MsClient addHandler(MessageType type, MessageHandler requestHandler) {
    this.handlers.put(type.getValue(), requestHandler);

    return this;
  }

  @Override
  public boolean sendMessage(Message msg) {
    boolean result = messageSystem.newMessage(msg);

    if (!result) {
      logger.error("the last message was rejected: {}", msg);
    }

    return result;
  }

  @Override
  public void handle(Message msg) {
    logger.info("new message:{}", msg);
    try {
      MessageHandler requestHandler = handlers.get(msg.getType());

      if (requestHandler != null) {
        requestHandler.handle(msg).ifPresent(this::sendMessage);
      } else {
        logger.error("handler not found for the message type:{}", msg.getType());
      }
    } catch (Exception ex) {
      logger.error("msg:" + msg, ex);
    }
  }

  @Override
  public <T> Message produceMessage(String to, T data, MessageType msgType, UUID identifier) {
    return new Message(getName(), to, identifier, msgType.getValue(), serializer.serialize(data));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MsClientImpl msClient = (MsClientImpl) o;
    return Objects.equals(getName(), msClient.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }
}
