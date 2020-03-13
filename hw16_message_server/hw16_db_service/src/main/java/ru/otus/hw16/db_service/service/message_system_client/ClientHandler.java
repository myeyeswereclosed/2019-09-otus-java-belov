package ru.otus.hw16.db_service.service.message_system_client;

import ru.otus.hw16.lib.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw16.lib.protocol.ProtocolMessage;
import ru.otus.hw16.lib.protocol.SourceType;
import ru.otus.hw16.db_service.service.user.CachedUserService;
import ru.otus.hw16.db_service.service.user.UserService;
import java.util.*;

@Component
public class ClientHandler {
    private UserService userService;

    @Autowired
    public ClientHandler(CachedUserService service) {
        userService = service;
    }

    public ProtocolMessage handle(ProtocolMessage message) {
        return
            new ProtocolMessage(
                message.getOriginId(),
                SourceType.DB_SERVICE.getType(),
                UUID.randomUUID(),
                message.getMessageType(),
                handlingResult(message)
            );
    }

    private List<User> handlingResult(ProtocolMessage message) {
        var emptyResult = new ArrayList<User>();

        if (message.isLogin() && !message.getUsers().isEmpty()) {
            User user = message.getUsers().get(0);

            return
                userService.getByCredentials(user.getLogin(), user.getPassword())
                    .map(Collections::singletonList)
                    .orElse(emptyResult)
                ;
        }

        if (message.isAllUsers()) {
            return userService.allUsers();
        }

        if (message.isCreateUser() && !message.getUsers().isEmpty()) {
            return
                userService.save(message.getUsers().get(0))
                    .map(Collections::singletonList)
                    .orElse(emptyResult)
                ;
        }

        return emptyResult;
    }
}
