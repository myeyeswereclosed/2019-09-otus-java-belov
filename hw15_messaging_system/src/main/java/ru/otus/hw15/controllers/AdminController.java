package ru.otus.hw15.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.hw15.domain.User;
import ru.otus.hw15.messagesystem.handler.front.FrontendService;
import ru.otus.hw15.messagesystem.*;
import ru.otus.hw15.messagesystem.client.MsClient;
import ru.otus.hw15.messagesystem.handler.MessageHandler;

import java.util.UUID;

@Controller
public class AdminController {
  private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

  private SimpMessagingTemplate template;
  private FrontendService frontendService;

  @Autowired
  public AdminController(
      @Qualifier("userDataRequestHandler") MessageHandler requestHandler,
      @Qualifier("userDataResponseHandler") MessageHandler responseHandler,
      MessageSystem messageSystem,
      @Qualifier("frontendMsClient") MsClient frontendClient,
      @Qualifier("userMsClient") MsClient userClient,
      SimpMessagingTemplate template,
      FrontendService frontendService
  ) {
      this.template = template;
      this.frontendService = frontendService;

    messageSystem
        .addClient(userClient.addHandler(MessageType.NEW_USER, requestHandler))
        .addClient(frontendClient.addHandler(MessageType.NEW_USER, responseHandler))
    ;
  }

  @MessageMapping("/users/create")
  public void addUser(User user) {
    logger.info("got User:{} ", user);

    UUID identifier = UUID.randomUUID();

    frontendService.getUserData(
        user,
        userData -> logger.info("{} send to message system", user),
        "user",
        identifier
    );

    frontendService.userSaved(
        identifier,
        userData -> template.convertAndSend("/users/added", userData)
    );
  }
}
