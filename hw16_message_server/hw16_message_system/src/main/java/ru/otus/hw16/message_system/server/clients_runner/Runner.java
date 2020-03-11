package ru.otus.hw16.message_system.server.clients_runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.hw16.db_service.DbServiceMain;
import ru.otus.hw16.frontend_client.FrontendMain;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class Runner implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger( Runner.class );

  private static final List<Client> clients =
    Arrays.asList(
      new Client("DbService", targetDir(DbServiceMain.class), "hw16_db_service.jar", Arrays.asList(7777, 7778)),
      new Client("FrontendClient", targetDir(FrontendMain.class), "hw16_frontend_client.jar", Arrays.asList(8080, 8082))
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
      logger.info("Starting client {} on port {}", client.name, port);

      try {
          new ProcessBuilder("java", "-jar", client.jar, "--server.port=" + port)
              .inheritIO()
              .directory(new File(client.targetPath))
              .start()
          ;

          // to start Spring)
          Thread.sleep(TimeUnit.SECONDS.toMillis(10));
      } catch (Exception e) {
          logger.error(e.getMessage(), e);
      }
  }

  private static String targetDir(Class clazz) {
      return
          new File(
              clazz
                  .getProtectionDomain()
                  .getCodeSource()
                  .getLocation()
                  .getPath()
          )
              .getParent()
      ;
  }

  private static class Client {
    private String name;
    private String targetPath;
    private String jar;
    private List<Integer> ports;

    private Client(String name, String targetPath, String jar, List<Integer> ports) {
      this.name = name;
      this.targetPath = targetPath;
      this.jar = jar;
      this.ports = ports;
    }
  }
}
