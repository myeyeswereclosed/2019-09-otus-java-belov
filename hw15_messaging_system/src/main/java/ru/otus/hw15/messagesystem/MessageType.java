package ru.otus.hw15.messagesystem;

public enum MessageType {
  NEW_USER("newUser");

  private final String value;

  MessageType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
