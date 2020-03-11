package ru.otus.hw16.message_system.server.messagesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.hw16.lib.protocol.MessageType;
import ru.otus.hw16.message_system.server.protocol.Client;
import ru.otus.hw16.message_system.server.protocol.MessageSystemClient;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public final class MessageSystemImpl implements MessageSystem {
  private static final Logger logger = LoggerFactory.getLogger(MessageSystemImpl.class);
  private static final int MESSAGE_QUEUE_SIZE = 100_000;
  private static final int MSG_HANDLER_THREAD_LIMIT = 2;

  private final AtomicBoolean runFlag = new AtomicBoolean(true);

  private final Map<String, MessageSystemClient> clientMap = new ConcurrentHashMap<>();
  private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);

  private Runnable disposeCallback;

  private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> {
    Thread thread = new Thread(runnable);
    thread.setName("msg-processor-thread");
    return thread;
  });

  private final ExecutorService msgHandler = Executors.newFixedThreadPool(MSG_HANDLER_THREAD_LIMIT, new ThreadFactory() {
    private final AtomicInteger threadNameSeq = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable runnable) {
      Thread thread = new Thread(runnable);
      thread.setName("msg-handler-thread-" + threadNameSeq.incrementAndGet());
      return thread;
    }
  });

  public MessageSystemImpl() {
    start();
  }

  public MessageSystemImpl(boolean startProcessing) {
    if (startProcessing) {
      start();
    }
  }

  @Override
  public void start() {
    msgProcessor.submit(this::msgProcessor);
  }

  @Override
  public int currentQueueSize() {
    return messageQueue.size();
  }

  @Override
  public MessageSystem addClient(Client msClient) {
    if (!clientMap.containsKey(msClient.getId().toString())) {
      logger.info("new client:{}", msClient.toString());

      clientMap.put(msClient.getId().toString(), msClient);
    }

    return this;
  }

  @Override
  public void removeClient(String clientId) {
    MessageSystemClient removedClient = clientMap.remove(clientId);

    if (removedClient == null) {
      logger.warn("client not found: {}", clientId);
    } else {
      logger.info("removed client:{}", removedClient);
    }
  }

    @Override
  public boolean newMessage(Message msg) {
    logger.info("New message {}", msg);

    if (runFlag.get()) {
        return messageQueue.offer(msg);
    } else {
        logger.warn("MS is being shutting down... rejected:{}", msg);

        return false;
    }
  }

  @Override
  public void dispose() throws InterruptedException {
    logger.info("now in the messageQueue {} messages", currentQueueSize());

    runFlag.set(false);

    insertStopMessage();

    msgProcessor.shutdown();

    msgHandler.awaitTermination(60, TimeUnit.SECONDS);
  }

  @Override
  public void dispose(Runnable callback) throws InterruptedException {
    disposeCallback = callback;
    dispose();
  }

  private void msgProcessor() {
    logger.info("msgProcessor started, {}", currentQueueSize());

    while (runFlag.get()) {
      try {
        Message message = messageQueue.poll();

        if (message != null) {
          logger.debug(
              "Clients are: \r\n{}",
              clientMap.values().stream().map(MessageSystemClient::toString).collect(Collectors.joining("\r\n"))
          );

          logger.info("Message type is: {}", message.getType());

          if (!MessageType.isVoid(message.getType())) {
            findClient(message).ifPresentOrElse(
                client -> {
                    logger.info("Client {} was found for message {}", client.toString(), message);
                    msgHandler.submit(() -> handleMessage(client, message));
                },
                () -> logger.warn("Client not found for message {}", message)
            );
          }
        }

        Thread.sleep(200);
      } catch (InterruptedException ex) {
        logger.error(ex.getMessage(), ex);
        Thread.currentThread().interrupt();
      } catch (Exception ex) {
        logger.error(ex.getMessage(), ex);
      }
    }

    if (disposeCallback != null) {
      msgHandler.submit(disposeCallback);
    }

    msgHandler.submit(this::messageHandlerShutdown);

    logger.info("msgProcessor finished");
  }

  private Optional<MessageSystemClient> findClient(Message message) {
      var sourceId = message.getToId();

      if (sourceId != null) {
          return
              clientMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(sourceId.toString()))
                .findFirst()
                .map(Map.Entry::getValue)
              ;
      } else {
          return
              clientMap
                  .values()
                  .stream()
                  .filter(client -> client.getType().equals(message.getTo()))
                  .findAny();
      }
  }

  private void messageHandlerShutdown() {
    msgHandler.shutdown();
    logger.info("msgHandler has been shut down");
  }

  private void handleMessage(MessageSystemClient client, Message msg) {
    try {
      client.handle(msg);
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      logger.error("message:{}", msg);
    }
  }

  private void insertStopMessage() throws InterruptedException {
    boolean result = messageQueue.offer(Message.STOP_MESSAGE);

    while (!result) {
      Thread.sleep(100);
      result = messageQueue.offer(Message.STOP_MESSAGE);
    }
  }
}
