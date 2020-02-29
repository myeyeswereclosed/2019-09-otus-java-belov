package ru.otus.hw16.frontend_client.common;

import ru.otus.hw16.frontend_client.domain.User;

public class Message {
    private User user;
    private String messageType;

    public Message(User user, String messageType) {
        this.user = user;
        this.messageType = messageType;
    }

    public ProtocolMessage toProtocolMessage() {
        return
            new ProtocolMessage(
                "frontend", "login", "login=" + user.getLogin() + ";password=" + user.getPassword()
            );
    }
}
