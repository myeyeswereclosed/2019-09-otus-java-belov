package ru.otus.hw15.messagesystem.client;

import ru.otus.hw15.messagesystem.Message;
import ru.otus.hw15.messagesystem.MessageType;
import ru.otus.hw15.messagesystem.handler.MessageHandler;

import java.util.UUID;

public interface MsClient {

  MsClient addHandler(MessageType type, MessageHandler requestHandler);

  boolean sendMessage(Message msg);

  void handle(Message msg);

  String getName();

  <T> Message produceMessage(String to, T data, MessageType msgType, UUID identifier);
}
