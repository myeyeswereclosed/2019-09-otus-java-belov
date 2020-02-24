package ru.otus.hw15.messagesystem;

import ru.otus.hw15.messagesystem.client.MsClient;

public interface MessageSystem {

  MessageSystem addClient(MsClient msClient);

  void removeClient(String clientId);

  boolean newMessage(Message msg);

  void dispose() throws InterruptedException;

  void dispose(Runnable callback) throws InterruptedException;

  void start();

  int currentQueueSize();
}

