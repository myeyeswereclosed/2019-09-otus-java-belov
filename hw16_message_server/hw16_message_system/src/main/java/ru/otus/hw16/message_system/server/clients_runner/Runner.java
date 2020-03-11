package ru.otus.hw16.message_system.server.clients_runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class Runner implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger( Runner.class );

  private static final List<Client> clients =
    Arrays.asList(
      new Client(
        "/home/belov/projects/java/2019-09-otus-java-belov/hw16_message_server/hw16_db_service/target",
        "hw16_db_service.jar",
        Arrays.asList(7777, 7778)
      ),
      new Client(
        "/home/belov/projects/java/2019-09-otus-java-belov/hw16_message_server/hw16_frontend_client/target",
        "hw16_frontend_client.jar",
        Arrays.asList(8080, 8081)
      )
    );

  public void run() {
    clients.forEach(
        client ->
            client.ports.forEach(
                port -> startClient(client, port)
            )
    );
  }

  private void startClient(Client client, int port) {
      logger.info("Starting client");

      var currentDir = new File(client.targetPath);

      var procBuilder = new ProcessBuilder("java", "-jar", client.jar, "--server.port=" + port)
          .inheritIO()
          .directory(currentDir);

      try {
          var process = procBuilder.start();

          try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
              String line;

              while ((line = reader.readLine()) != null) {
                  logger.info("proc out: {}", line);
              }
          }

          Thread.sleep(TimeUnit.SECONDS.toMillis(10));
      } catch (Exception e) {
          logger.error(e.getMessage(), e);
      }
  }

  private static class Client {
    private String targetPath;
    private String jar;
    private List<Integer> ports;

    private Client(String targetPath, String jar, List<Integer> ports) {
      this.targetPath = targetPath;
      this.jar = jar;
      this.ports = ports;
    }
  }
}
