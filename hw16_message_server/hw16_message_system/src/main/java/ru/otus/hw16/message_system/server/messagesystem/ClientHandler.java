package ru.otus.hw16.message_system.server.messagesystem;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw16.lib.protocol.ProtocolMessage;
import ru.otus.hw16.message_system.server.protocol.Client;
import ru.otus.hw16.message_system.server.protocol.MessageSystemProtocol;
import java.io.*;
import java.net.Socket;
import java.util.Optional;

public class ClientHandler extends Thread {
    private static Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    private final Socket socket;
    private final MessageSystem messageSystem;
    private final MessageSystemProtocol protocol;
    private final Gson serializer;

    public ClientHandler(
        Socket socket,
        MessageSystem messageSystem,
        MessageSystemProtocol protocol,
        Gson serializer
    ) {
        this.socket = socket;
        this.protocol = protocol;
        this.messageSystem = messageSystem;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (!socket.isClosed()) {
                var protocolMessage = serializer.fromJson(in.readLine(), ProtocolMessage.class);

                Optional.ofNullable(protocolMessage).ifPresent(
                    clientMessage ->
                        protocol
                            .parse(clientMessage)
                            .ifPresentOrElse(
                                msg ->
                                    messageSystem
                                        .addClient(
                                            new Client(
                                                clientMessage.getOriginId(), msg.getFrom(), socket, serializer)
                                        )
                                        .newMessage(msg),
                                () -> logger.error("Message clients are not resolved for {}", clientMessage)
                            )
                );

                sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}

