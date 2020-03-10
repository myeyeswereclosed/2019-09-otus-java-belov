package ru.otus.hw16.message_system.server.server;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw16.message_system.server.messagesystem.MessageSystem;
import ru.otus.hw16.message_system.server.messagesystem.MessageSystemImpl;
import ru.otus.hw16.message_system.server.messagesystem.ClientHandler;
import ru.otus.hw16.message_system.server.protocol.MessageSystemProtocol;
import ru.otus.hw16.message_system.server.protocol.Protocol;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Server implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    private final MessageSystem messageSystem;
    private final MessageSystemProtocol protocol;
    private final Gson serializer;

    @Autowired
    public Server(
        MessageSystemImpl messageSystem,
        Protocol protocol,
        Gson serializer
    ) {
        this.messageSystem = messageSystem;
        this.protocol = protocol;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        try (var server = new ServerSocket(5555)) {

            logger.info("Server started!");

            messageSystem.start();

            while(!server.isClosed()) {
                try {
                    Socket clientSocket = server.accept();

                    logger.info("Client {}:{} connected", clientSocket.getInetAddress(), clientSocket.getPort());

                    executor.execute(new ClientHandler(clientSocket, messageSystem, protocol, serializer));
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }

                Thread.sleep(100);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
