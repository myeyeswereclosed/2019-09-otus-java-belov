package ru.otus.hw16.db_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.hw16.db_service.service.message_system_client.Client;

@SpringBootApplication
public class DbServiceMain implements CommandLineRunner {
  private Client userClient;

  public static void main(String[] args) {
    SpringApplication.run(DbServiceMain.class, args);
  }

  public DbServiceMain(Client userClient) {
    this.userClient = userClient;
  }

  @Override
  public void run(String[] args) {
    userClient.run();
  }
}
