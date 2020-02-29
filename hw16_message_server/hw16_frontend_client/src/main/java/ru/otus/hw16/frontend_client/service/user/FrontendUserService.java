package ru.otus.hw16.frontend_client.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.hw16.frontend_client.Client;
import ru.otus.hw16.frontend_client.common.ProtocolMessage;
import ru.otus.hw16.frontend_client.domain.User;
import java.util.List;
import java.util.Optional;

@Service
public class FrontendUserService implements UserService {
    private Client client;

    @Autowired
    public FrontendUserService(Client client) {
        this.client = client;
    }

    @Override
    public Optional<User> save(User user) {
        return Optional.empty();
    }

    @Override
    public List<User> allUsers() {
        return null;
    }

    @Override
    public List<User> clients() {
        return null;
    }

    @Override
    public Optional<User> getByCredentials(String login, String password) {
        client.start();
        client.sendMessage(
            new ProtocolMessage(
                "frontend", "login", "login=" + login + ";password=" + password
            ).toString()
        );

        return Optional.empty();
    }
}
