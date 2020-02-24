package ru.otus.hw15.messagesystem.handler;


import ru.otus.hw15.messagesystem.Message;

import java.util.Optional;

public interface MessageHandler {
  Optional<Message> handle(Message msg);
}
