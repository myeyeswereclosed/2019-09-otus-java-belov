package ru.otus.hw12.servlet.admin;

import ru.otus.hw12.model.User;
import ru.otus.hw12.services.common.UserService;
import ru.otus.hw12.services.template.TemplateProcessor;
import ru.otus.hw12.servlet.BaseServlet;
import ru.otus.hw12.servlet.ContentType;
import ru.otus.hw12.servlet.TemplatePage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

public class UsersServlet extends BaseServlet {
    private final UserService userService;
    private final TemplateProcessor templateProcessor;
    private static final String USERS_KEY = "users";

    public UsersServlet(UserService userService, TemplateProcessor templateProcessor) {
        this.userService = userService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        templateResponse(
            response,
            ContentType.TEXT_HTML,
            templateProcessor,
            TemplatePage.USERS,
            Collections.singletonMap(
                USERS_KEY,
                userService.allUsers().stream().map(User::toMap).collect(Collectors.toList())
            )
        );
    }
}
