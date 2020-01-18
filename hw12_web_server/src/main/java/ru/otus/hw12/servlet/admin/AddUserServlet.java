package ru.otus.hw12.servlet.admin;

import ru.otus.hw12.model.User;
import ru.otus.hw12.model.UserRole;
import ru.otus.hw12.services.hash.HashService;
import ru.otus.hw12.services.template.TemplateProcessor;
import ru.otus.hw12.services.common.UserService;
import ru.otus.hw12.servlet.BaseServlet;
import ru.otus.hw12.servlet.ContentType;
import ru.otus.hw12.servlet.TemplatePage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class AddUserServlet extends BaseServlet {
    private static final String PARAM_NAME = "name";
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_ROLE = "role";

    private final TemplateProcessor templateProcessor;
    private final UserService userService;

    public AddUserServlet(UserService userService, TemplateProcessor templateProcessor) {
        this.userService = userService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        templateResponse(
            response,
            ContentType.TEXT_HTML,
            templateProcessor,
            TemplatePage.ADD_USER,
            Collections.singletonMap("roles", UserRole.values())
        );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User userFromRequest = userFromRequest(request);
        Optional<User> user = userService.save(userFromRequest);

        if (user.isPresent()) {
            makeResponse(response, TemplatePage.USER_ADDED, user.get().toMap());
        } else {
            makeResponse(response, TemplatePage.USER_NOT_ADDED, userFromRequest.toMap());
        }
    }

    private void makeResponse(
        HttpServletResponse response,
        TemplatePage templatePage,
        Map<String, Object> params
    ) throws IOException {
        templateResponse(response, ContentType.TEXT_HTML, templateProcessor, templatePage, params);
    }

    private User userFromRequest(HttpServletRequest request) {
        return
            new User(
                requestParam(request, PARAM_NAME),
                requestParam(request, PARAM_LOGIN),
                HashService.hash(requestParam(request, PARAM_PASSWORD)),
                UserRole.valueOf(requestParam(request, PARAM_ROLE))
            );
    }
}
