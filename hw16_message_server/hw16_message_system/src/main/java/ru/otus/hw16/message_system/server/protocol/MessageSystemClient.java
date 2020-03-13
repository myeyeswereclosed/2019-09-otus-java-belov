package ru.otus.hw16.message_system.server.protocol;

import ru.otus.hw16.message_system.server.messagesystem.Message;

public interface MessageSystemClient {
    void handle(Message message);

    String getType();
}
