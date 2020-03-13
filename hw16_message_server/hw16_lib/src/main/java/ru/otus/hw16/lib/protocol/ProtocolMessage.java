package ru.otus.hw16.lib.protocol;

import ru.otus.hw16.lib.domain.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtocolMessage implements Serializable, Comparable {
    private static final long serialVersionUID = 1L;

    private UUID originId;
    private String senderType;

    private UUID messageId;
    private String messageType;

    private List<User> users = new ArrayList<>();

    public ProtocolMessage(
        UUID senderId,
        String clientType,
        UUID messageId,
        String messageType,
        List<User> users
    ) {
        this(senderId, clientType, messageId, messageType);

        this.users = users;
    }

    public ProtocolMessage(
        UUID senderId,
        String clientType,
        UUID messageId,
        String messageType
    ) {
        this.originId = senderId;
        this.messageId = messageId;
        this.senderType = clientType;
        this.messageType = messageType;
    }

    public UUID getOriginId() {
        return originId;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public List<User> getUsers() {
        return users;
    }

    public String getSenderType() {
        return senderType;
    }

    public String getMessageType() {
        return messageType;
    }

    public boolean isLogin() {
        return MessageType.LOGIN.getValue().equals(messageType);
    }

    public boolean isAllUsers() {
        return MessageType.ALL_USERS.getValue().equals(messageType);
    }

    public boolean isCreateUser() {
        return MessageType.NEW_USER.getValue().equals(messageType);
    }

    @Override
    public String toString() {
        return senderType + ';' + originId + ';' + messageType + ';' + users + "\r\n";
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
