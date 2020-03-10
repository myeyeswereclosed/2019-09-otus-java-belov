package ru.otus.hw16.frontend_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.hw16.frontend_client.service.message_system_client.Client;

@SpringBootApplication
public class FrontendMain implements CommandLineRunner {
    private Client client;

    public static void main(String[] args) {
    SpringApplication.run(FrontendMain.class, args);
  }

    @Autowired
    public FrontendMain(Client client) {
        this.client = client;
    }

    @Override
    public void run(String[] args) {
        client.run();
    }
}
