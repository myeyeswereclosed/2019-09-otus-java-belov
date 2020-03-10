package ru.otus.hw16.message_system.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw16.message_system.server.clients_runner.Runner;
import ru.otus.hw16.message_system.server.server.Server;
import java.util.concurrent.Executors;

@SpringBootApplication
@Configuration
public class MessageSystemMain implements CommandLineRunner {
    private Server server;
    private Runner clientsRunner;

    public static void main(String[] args) {
        SpringApplication.run(MessageSystemMain.class, args);
    }

    @Autowired
    public MessageSystemMain(Server server, Runner clientsRunner) {
        this.server = server;
        this.clientsRunner = clientsRunner;
    }

    public void run(String[] args) {
        var executorService = Executors.newFixedThreadPool(2);

        executorService.execute(server);
        executorService.execute(clientsRunner);
    }
}
