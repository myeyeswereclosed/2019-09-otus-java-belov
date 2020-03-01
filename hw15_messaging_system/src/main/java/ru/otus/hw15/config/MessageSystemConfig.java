package ru.otus.hw15.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw15.common.Serializer;
import ru.otus.hw15.messagesystem.MessageSystem;
import ru.otus.hw15.messagesystem.MessageSystemImpl;
import ru.otus.hw15.messagesystem.MessageType;
import ru.otus.hw15.messagesystem.client.FrontendClient;
import ru.otus.hw15.messagesystem.client.MsClient;
import ru.otus.hw15.messagesystem.client.UserClient;
import ru.otus.hw15.messagesystem.handler.MessageHandler;
import ru.otus.hw15.messagesystem.handler.UserDataRequestHandler;
import ru.otus.hw15.messagesystem.handler.front.FrontendService;
import ru.otus.hw15.messagesystem.handler.front.FrontendServiceImpl;
import ru.otus.hw15.messagesystem.handler.front.handlers.UserDataResponseHandler;
import ru.otus.hw15.service.user.CachedUserService;
import ru.otus.hw15.service.user.UserService;

import javax.annotation.PostConstruct;

@Configuration
public class MessageSystemConfig {
    private final UserService userService;
    private final Serializer serializer;

    @Autowired
    public MessageSystemConfig(CachedUserService userService, Serializer serializer) {
        this.userService = userService;
        this.serializer = serializer;
    }

    @Bean
    public MessageHandler requestHandler() {
        return new UserDataRequestHandler(userService, serializer);
    }

    @Bean
    public MsClient userClient() {
        return new UserClient(messageSystem(), serializer);
    }

    @Bean
    public FrontendClient frontendClient() {
        return new FrontendClient(messageSystem(), serializer);
    }

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public MessageHandler responseHandler() {
        return new UserDataResponseHandler(frontendService(), serializer);
    }

    @Bean
    public FrontendService frontendService() {
        return new FrontendServiceImpl(frontendClient());
    }

    @PostConstruct
    public void initMessageSystem() {
        messageSystem()
            .addClient(userClient().addHandler(MessageType.NEW_USER, requestHandler()))
            .addClient(frontendClient().addHandler(MessageType.NEW_USER, responseHandler()))
        ;
    }
}
