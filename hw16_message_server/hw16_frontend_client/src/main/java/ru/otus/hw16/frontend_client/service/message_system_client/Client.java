package ru.otus.hw16.frontend_client.service.message_system_client;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw16.lib.protocol.ProtocolMessage;
import java.io.*;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class Client implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    @Value("${message_system.port}")
    private int port = 5555;

    @Value("${message_system.host}")
    private String host;

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private PriorityBlockingQueue<ProtocolMessage> incomingMessages = new PriorityBlockingQueue<>();
    private PriorityBlockingQueue<ProtocolMessage> outgoingMessages = new PriorityBlockingQueue<>();

    private Gson serializer;

    @Autowired
    public Client(Gson serializer) {
        this.serializer = serializer;
    }

    public Client sendMessage(ProtocolMessage message) {
        incomingMessages.put(message);

        return this;
    }

    public ProtocolMessage takeMessage() throws InterruptedException {
        return outgoingMessages.take();
    }

    @Override
    public void run() {
        logger.info("Starting frontend client");

        try (
            var socket = new Socket(host, port);
            var toMessageSystem = new PrintWriter(socket.getOutputStream(), true);
            var fromMessageSystem = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            isRunning.set(true);

            while (isRunning.get()) {
                var request = incomingMessages.take();

                logger.info("Sending to message system: {}", request);

                toMessageSystem.println(serializer.toJson(request));

                String response = fromMessageSystem.readLine();

                logger.info("Server responded with: {}", response);

                Optional
                    .ofNullable(response)
                    .ifPresent(
                        validResponse ->
                            outgoingMessages.put(serializer.fromJson(validResponse, ProtocolMessage.class))
                    );

                Thread.sleep(200);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
