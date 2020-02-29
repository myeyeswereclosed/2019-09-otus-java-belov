package ru.otus.hw16.frontend_client.common;

import java.io.Serializable;

public class ProtocolMessage implements Serializable {
    private String clientType;
    private String messageType;
    private String body;

    public ProtocolMessage(String clientType, String messageType, String body) {
        this.clientType = clientType;
        this.messageType = messageType;
        this.body = body;
    }

    @Override
    public String toString() {
        return clientType + ';' + messageType + ';' + body + "\r\n";
    }
}
