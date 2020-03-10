package ru.otus.hw16.message_system.server.messagesystem;

import ru.otus.hw16.message_system.server.protocol.Client;

public interface MessageSystem {

  MessageSystem addClient(Client msClient);

  void removeClient(String clientId);

  boolean newMessage(Message msg);

  void dispose() throws InterruptedException;

  void dispose(Runnable callback) throws InterruptedException;

  void start();

  int currentQueueSize();
}

