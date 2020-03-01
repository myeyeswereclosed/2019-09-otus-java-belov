package ru.otus.hw15.messagesystem.client;

import org.springframework.stereotype.Component;
import ru.otus.hw15.common.Serializer;
import ru.otus.hw15.messagesystem.MessageSystem;

public class UserClient extends MsClientImpl {
    public UserClient(MessageSystem messageSystem, Serializer serializer) {
        super(messageSystem, serializer);
    }

    @Override
    public String getName() {
        return "user";
    }
}
