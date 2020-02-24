package ru.otus.hw15.messagesystem.client;

import org.springframework.stereotype.Component;
import ru.otus.hw15.common.Serializer;
import ru.otus.hw15.messagesystem.MessageSystem;

@Component("frontendMsClient")
public class FrontendClient extends MsClientImpl {
    public FrontendClient(MessageSystem messageSystem, Serializer serializer) {
        super(messageSystem, serializer);
    }

    @Override
    public String getName() {
        return "frontend";
    }
}
