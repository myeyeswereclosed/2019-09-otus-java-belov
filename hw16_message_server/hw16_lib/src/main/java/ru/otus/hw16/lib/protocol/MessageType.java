package ru.otus.hw16.lib.protocol;

public enum MessageType {
  LOGIN("login"),
  ALL_USERS("allUsers"),
  NEW_USER("newUser"),
  VOID("void");

  private final String value;

  MessageType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static boolean isVoid(String type) {
    return VOID.value.equals(type);
  }
}
