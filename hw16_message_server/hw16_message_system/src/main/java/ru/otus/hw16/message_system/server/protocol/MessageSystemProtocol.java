package ru.otus.hw16.message_system.server.protocol;

import ru.otus.hw16.lib.protocol.ProtocolMessage;
import ru.otus.hw16.message_system.server.messagesystem.Message;
import java.util.Optional;

public interface MessageSystemProtocol {
    Optional<Message> parse(ProtocolMessage protocolMessage);
}
