package ru.otus.hw12.servlet;

import ru.otus.hw12.UserServer;
import ru.otus.hw12.model.User;
import ru.otus.hw12.services.template.TemplateProcessor;
import ru.otus.hw12.services.auth.UserAuthService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class LoginServlet extends BaseServlet {
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 30;

    private final TemplateProcessor templateProcessor;
    private final UserAuthService userAuthService;

    public LoginServlet(TemplateProcessor templateProcessor, UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        noParamsResponse(response, ContentType.TEXT_HTML, templateProcessor, TemplatePage.LOGIN);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<User> authenticated =
            userAuthService.authenticated(
                requestParam(request, PARAM_LOGIN),
                requestParam(request, PARAM_PASSWORD)
            );

        if (authenticated.isPresent() && authenticated.get().isAdmin()) {
            request.getSession().setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect(UserServer.ADMIN);
        } else {
            response.setStatus(SC_UNAUTHORIZED);
        }
    }
}
