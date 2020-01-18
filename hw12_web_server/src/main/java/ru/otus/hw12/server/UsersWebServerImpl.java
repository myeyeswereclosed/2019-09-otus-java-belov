package ru.otus.hw12.server;

import com.google.gson.Gson;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import ru.otus.hw12.UserServer;
import ru.otus.hw12.api.dao.UserDao;
import ru.otus.hw12.helpers.FileSystemHelper;
import ru.otus.hw12.services.template.TemplateProcessor;
import ru.otus.hw12.services.auth.UserAuthService;
import ru.otus.hw12.services.common.UserService;
import ru.otus.hw12.servlet.*;
import ru.otus.hw12.servlet.admin.AddUserServlet;
import ru.otus.hw12.servlet.admin.AdminServlet;
import ru.otus.hw12.servlet.admin.UsersServlet;

import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class UsersWebServerImpl implements UsersWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    private static final String ROLE_NAME_USER = "user";
    private static final String ROLE_NAME_ADMIN = "admin";
    private static final String CONSTRAINT_NAME = "auth";

    private final int port;
    private final SecurityType securityType;
    private final UserAuthService userAuthServiceForFilterBasedSecurity;
    private final UserService userService;
    private final LoginService loginServiceForBasicSecurity;
    private final UserDao userDao;
    private final TemplateProcessor templateProcessor;
    private final Server server;

    public UsersWebServerImpl(
        int port,
        SecurityType securityType,
        UserAuthService userAuthServiceForFilterBasedSecurity,
        UserService userService,
        LoginService loginServiceForBasicSecurity,
        UserDao userDao,
        TemplateProcessor templateProcessor
    ) {
        this.port = port;
        this.securityType = securityType;
        this.userAuthServiceForFilterBasedSecurity = userAuthServiceForFilterBasedSecurity;
        this.userService = userService;
        this.loginServiceForBasicSecurity = loginServiceForBasicSecurity;
        this.userDao = userDao;
        this.templateProcessor = templateProcessor;

        server = initContext();
    }

    @Override
    public void start() throws Exception {
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {
        HandlerList handlers = new HandlerList();

        ResourceHandler resourceHandler = createResourceHandler();
        handlers.addHandler(resourceHandler);

        ServletContextHandler servletContextHandler = createServletContextHandler();
        handlers.addHandler(applySecurity(servletContextHandler));

        Server srv = new Server(port);
        srv.setHandler(handlers);
        return srv;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        var handler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        addServlet(handler, new AdminServlet(templateProcessor), UserServer.ADMIN).
        addServlet(handler, new AddUserServlet(userService, templateProcessor), UserServer.ADD_USER).
        addServlet(handler, new UsersServlet(userService, templateProcessor), UserServer.USERS);

        return handler;
    }

    private UsersWebServerImpl addServlet(ServletContextHandler handler, HttpServlet servlet, String path) {
        handler.addServlet(new ServletHolder(servlet), path);

        return this;
    }

    private Handler applySecurity(ServletContextHandler servletContextHandler) {
        if (securityType == SecurityType.NONE){
            return servletContextHandler;
        } else if (securityType == SecurityType.FILTER_BASED) {
            applyFilterBasedSecurity(servletContextHandler, UserServer.secured);
            return servletContextHandler;
        } else if (securityType == SecurityType.BASIC) {
            return createBasicAuthSecurityHandler(servletContextHandler, UserServer.secured);
        } else {
            throw new InvalidSecurityTypeException(securityType);
        }
    }

    private ServletContextHandler applyFilterBasedSecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(
            new ServletHolder(
                new LoginServlet(templateProcessor, userAuthServiceForFilterBasedSecurity)
            ),
            UserServer.LOGIN
        );

        AuthorizationFilter authorizationFilter = new AuthorizationFilter();

        IntStream.range(0, paths.length)
                .forEachOrdered(i -> servletContextHandler.addFilter(
                    new FilterHolder(authorizationFilter), paths[i], null)
                );

        return servletContextHandler;
    }

    private SecurityHandler createBasicAuthSecurityHandler(ServletContextHandler context, String... paths) {
        Constraint constraint = new Constraint();
        constraint.setName(CONSTRAINT_NAME);
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{ROLE_NAME_USER, ROLE_NAME_ADMIN});

        List<ConstraintMapping> constraintMappings = new ArrayList<>();
        IntStream.range(0, paths.length).forEachOrdered(i -> {
            ConstraintMapping mapping = new ConstraintMapping();
            mapping.setPathSpec(paths[i]);
            mapping.setConstraint(constraint);
            constraintMappings.add(mapping);
        });

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        //как декодировать стороку с юзером:паролем https://www.base64decode.org/
        security.setAuthenticator(new BasicAuthenticator());

        security.setLoginService(loginServiceForBasicSecurity);
        security.setConstraintMappings(constraintMappings);
        security.setHandler(new HandlerList(context));

        return security;
    }

}
