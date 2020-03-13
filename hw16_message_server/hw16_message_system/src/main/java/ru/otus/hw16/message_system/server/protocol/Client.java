package ru.otus.hw16.message_system.server.protocol;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw16.lib.protocol.ProtocolMessage;
import ru.otus.hw16.message_system.server.messagesystem.Message;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class Client implements MessageSystemClient {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private final UUID id;
    private final String type;
    private final Socket socket;
    private final Gson serializer;
    private PrintWriter outputWriter;

    public Client(UUID id, String type, Socket socket, Gson serializer) {
        this.id = id;
        this.type = type;
        this.socket = socket;
        this.serializer = serializer;

        try {
            outputWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void handle(Message message) {
        outputWriter.println(
            serializer.toJson(
                new ProtocolMessage(
                    message.getSourceId(),
                    message.getFrom(),
                    message.getSourceMessageId(),
                    message.getType(),
                    message.getUsers()
                )
            )
        );
    }

    @Override
    public String toString() {
        return
            "{id: " + id.toString() + "; type: " + type +
            "; from: " + socket.getInetAddress() + ":" + socket.getPort() + '}'
        ;
    }
}
