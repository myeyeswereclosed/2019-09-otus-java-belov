package ru.otus.hw16.frontend_client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw16.lib.hash.HashService;
import ru.otus.hw16.lib.hash.HashingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.hw16.frontend_client.service.message_system_client.Client;
import ru.otus.hw16.lib.protocol.MessageType;
import ru.otus.hw16.lib.protocol.ProtocolMessage;
import ru.otus.hw16.lib.domain.User;
import ru.otus.hw16.lib.protocol.SourceType;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class FrontendUserService implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(FrontendUserService.class);

    private final Client client;
    private final HashService hashService;
    private final UUID id = UUID.randomUUID();

    @Autowired
    public FrontendUserService(Client client, HashingService hashService) {
        this.client = client;
        this.hashService = hashService;
    }

    @Override
    public void save(User user, Consumer<User> consumer) {
        hashService.hash(user.getPassword()).ifPresentOrElse(
            password -> handleNewUser(user, password, consumer),
            () -> logger.warn("Error with password hashing for user {}", user)
        );
    }

    private void handleNewUser(User user, String hashedPassword, Consumer<User> consumer) {
        try {
            user.setPassword(hashedPassword);

            List<User> result =
                client
                    .sendMessage(createMessage(MessageType.NEW_USER, Collections.singletonList(user)))
                    .takeMessage()
                    .getUsers()
                ;

            if (!result.isEmpty()) {
                consumer.accept(result.get(0));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public List<User> allUsers() {
        client.sendMessage(createMessage(MessageType.ALL_USERS, Collections.emptyList()));

        try {
            return client.takeMessage().getUsers();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return Collections.emptyList();
    }

    @Override
    public List<User> clients() {
        return
            allUsers()
                .stream()
                .filter(User::isClient)
                .collect(Collectors.toList())
            ;
    }

    @Override
    public Optional<User> getByCredentials(String login, String password) {
        User user = new User();

        Optional<String> hashedPassword = hashService.hash(password);

        if (hashedPassword.isEmpty()) {
            return Optional.empty();
        }

        user.setLogin(login);
        user.setPassword(hashedPassword.get());

        client.sendMessage(createMessage(MessageType.LOGIN, Collections.singletonList(user)));

        try {
            return Optional.ofNullable(client.takeMessage().getUsers().get(0));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return Optional.empty();
    }

    private ProtocolMessage createMessage(MessageType type, List<User> users) {
        return
            new ProtocolMessage(
                id,
                SourceType.FRONTEND.getType(),
                UUID.randomUUID(),
                type.getValue(),
                users
            );
    }
}
