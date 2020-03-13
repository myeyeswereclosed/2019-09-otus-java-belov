package ru.otus.hw16.message_system.server.protocol;

import org.springframework.stereotype.Component;
import ru.otus.hw16.lib.protocol.ProtocolMessage;
import ru.otus.hw16.lib.protocol.SourceType;
import ru.otus.hw16.message_system.server.messagesystem.Message;
import java.util.Optional;
import java.util.UUID;

@Component
public class Protocol implements MessageSystemProtocol {
    // for frontend message it doesn't matter to which backend it will be send
    public Optional<Message> parse(ProtocolMessage protocolMessage) {
        return
            Optional
                .ofNullable(protocolMessage.getSenderType())
                .flatMap(
                    type -> {
                        if (SourceType.isFrontend(type)) {
                            return Optional.of(
                                make(SourceType.FRONTEND, SourceType.DB_SERVICE, null, protocolMessage)
                            );
                        }

                        if (SourceType.isDbService(type)) {
                            return
                                Optional.of(
                                    make(
                                        SourceType.DB_SERVICE,
                                        SourceType.FRONTEND,
                                        protocolMessage.getOriginId(),
                                        protocolMessage
                                    )
                                );
                        }

                        return Optional.empty();
                    }
                );
    }

    private Message make(SourceType from, SourceType to, UUID targetId, ProtocolMessage protocolMessage) {
        return
            new Message(
                from.getType(),
                to.getType(),
                protocolMessage.getMessageId(),
                protocolMessage.getOriginId(),
                targetId,
                protocolMessage.getMessageType(),
                protocolMessage.getUsers()
            );
    }
}
