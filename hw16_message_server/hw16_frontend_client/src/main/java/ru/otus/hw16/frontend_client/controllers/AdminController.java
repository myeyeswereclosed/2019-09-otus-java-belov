package ru.otus.hw16.frontend_client.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.hw16.frontend_client.service.UserService;
import ru.otus.hw16.lib.domain.User;

@Controller
public class AdminController {
  private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

  private SimpMessagingTemplate template;
  private UserService userService;

  @Autowired
  public AdminController(UserService userService, SimpMessagingTemplate template) {
      this.template = template;
      this.userService = userService;
  }

  @MessageMapping("/users/create")
  public void addUser(User user) {
    logger.info("got User:{} ", user.toString());

    userService.save(
        user,
        userSaved -> template.convertAndSend("/users/added", userSaved)
    );
  }
}
