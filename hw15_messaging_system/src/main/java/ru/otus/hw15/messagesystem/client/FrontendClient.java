package ru.otus.hw15.messagesystem.client;

import ru.otus.hw15.common.Serializer;
import ru.otus.hw15.messagesystem.MessageSystem;

public class FrontendClient extends MsClientImpl {
    public FrontendClient(MessageSystem messageSystem, Serializer serializer) {
        super(messageSystem, serializer);
    }

    @Override
    public String getName() {
        return "frontend";
    }
}
