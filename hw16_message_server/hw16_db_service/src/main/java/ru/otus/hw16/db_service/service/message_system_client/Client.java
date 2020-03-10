package ru.otus.hw16.db_service.service.message_system_client;

import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw16.lib.protocol.MessageType;
import ru.otus.hw16.lib.protocol.ProtocolMessage;
import ru.otus.hw16.lib.protocol.SourceType;
import java.io.*;
import java.net.Socket;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class Client implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(Client.class);

    @Value("${message_system.port}")
    private int port = 5555;

    @Value("${message_system.host}")
    private String host;

    private PriorityBlockingQueue<ProtocolMessage> incomingMessages = new PriorityBlockingQueue<>();

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private ClientHandler handler;
    private GsonBuilder serializer;

    private UUID id = UUID.randomUUID();

    @Autowired
    public Client(ClientHandler handler, GsonBuilder serializer) {
        this.handler = handler;
        this.serializer = serializer;
    }

    public void run() {
        logger.info("Starting dbService client");

        try (
            var socket = new Socket(host, port);
            var toMessageSystem = new PrintWriter(socket.getOutputStream(), true);
            var fromMessageSystem = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            isRunning.set(true);

            while (isRunning.get()) {
                var request =
                    Optional
                        .ofNullable(incomingMessages.poll())
                        .orElse(
                            new ProtocolMessage(
                                id,
                                SourceType.DB_SERVICE.getType(),
                                UUID.randomUUID(),
                                MessageType.VOID.getValue())
                        );

                logger.info("Sending to server: {}", request);

                toMessageSystem.println(serializer.create().toJson(request));

                var response =
                    serializer.disableHtmlEscaping().create().fromJson(
                        fromMessageSystem.readLine(),
                        ProtocolMessage.class
                    );

                logger.info("Server responded with: {}", response);

                Optional
                    .ofNullable(response)
                    .ifPresent(validResponse -> incomingMessages.put(handler.handle(response)));

                Thread.sleep(200);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
