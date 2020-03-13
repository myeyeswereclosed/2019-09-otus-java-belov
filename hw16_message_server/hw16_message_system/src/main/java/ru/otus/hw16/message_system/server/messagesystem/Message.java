package ru.otus.hw16.message_system.server.messagesystem;

import ru.otus.hw16.lib.domain.User;

import java.util.*;

public class Message {
  static final Message STOP_MESSAGE = new Message();

  private final UUID id = UUID.randomUUID();
  private final String from;
  private final UUID sourceId;

  private final String to;
  private final UUID toId;

  private final UUID sourceMessageId;
  private final String type;

  private List<User> users = new ArrayList<>();

  public Message() {
    this.from = null;
    this.sourceId = null;
    this.sourceMessageId = UUID.randomUUID();

    this.to = null;
    this.toId = null;

    this.type = "voidTechnicalMessage";
  }

  public Message(
      String from,
      String to,
      UUID sourceMessageId,
      UUID sourceId,
      UUID toId,
      String type,
      List<User> users
  ) {
    this.from = from;
    this.sourceId = sourceId;
    this.to = to;
    this.toId = toId;
    this.sourceMessageId = sourceMessageId;
    this.type = type;
    this.users = users;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Message message = (Message) o;
    return id == message.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Message{" +
        "id=" + id +
        ", from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", sourceId='" + sourceId + '\'' +
        ", targetSourceId='" + toId + '\'' +
        ", sourceMessageId=" + sourceMessageId +
        ", type='" + type + '\'' + ", users = " + users +
        '}';
  }

  public UUID getId() {
    return id;
  }

  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }

  public String getType() {
    return type;
  }

  public List<User> getUsers() {
    return users;
  }

  public UUID getSourceMessageId() {
    return sourceMessageId;
  }

  public UUID getSourceId() {
    return sourceId;
  }

  public UUID getToId() {
    return toId;
  }
}
