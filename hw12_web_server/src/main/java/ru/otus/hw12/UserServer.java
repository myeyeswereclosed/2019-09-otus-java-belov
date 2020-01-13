package ru.otus.hw12;

import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw12.api.dao.UserDao;
import ru.otus.hw12.api.dao.hibernate.HibernateUserDao;
import ru.otus.hw12.api.dao.hibernate.HibernateUtils;
import ru.otus.hw12.api.sessionmanager.hibernate.SessionManagerHibernate;
import ru.otus.hw12.helpers.FileSystemHelper;
import ru.otus.hw12.model.User;
import ru.otus.hw12.model.UserRole;
import ru.otus.hw12.server.UsersWebServer;
import ru.otus.hw12.server.UsersWebServerImpl;
import ru.otus.hw12.services.hash.HashService;
import ru.otus.hw12.services.template.TemplateProcessorImpl;
import ru.otus.hw12.services.auth.UserAuthServiceImpl;
import ru.otus.hw12.services.common.CommonUserService;
import ru.otus.hw12.services.common.UserService;

import static ru.otus.hw12.server.SecurityType.FILTER_BASED;

public class UserServer {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String HIBERNATE_CONFIG_FILE = "hibernate.cfg.xml";
    private static final String REALM_NAME = "AnyRealm";

    private static final User TEST_ADMIN =
        new User(
            "Иван Иваныч",
            "test_admin",
            HashService.hash("123456"),
            UserRole.ADMIN
        );

    private static final Logger logger = LoggerFactory.getLogger(UserServer.class);

    public static void main(String[] args) throws Exception {
        String hashLoginServiceConfigPath =
            FileSystemHelper.localFileNameOrResourceNameToFullPath(
                HASH_LOGIN_SERVICE_CONFIG_NAME
            );

//      UserDao userDao = new InMemoryUserDao();
        UserDao userDao = userDao();
        LoginService loginServiceForBasicSecurity = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);
//      LoginService loginServiceForBasicSecurity = new InMemoryLoginServiceImpl(userDao);

        UsersWebServer usersWebServer =
            new UsersWebServerImpl(
                WEB_SERVER_PORT,
                FILTER_BASED,
                new UserAuthServiceImpl(userDao, logger),
                userService(userDao),
                loginServiceForBasicSecurity,
                userDao,
                new GsonBuilder().serializeNulls().setPrettyPrinting().create(),
                new TemplateProcessorImpl(TEMPLATES_DIR)
            );

        usersWebServer.start();
        usersWebServer.join();
    }

    private static UserDao userDao() {
        return
            new HibernateUserDao(
                new SessionManagerHibernate(
                    HibernateUtils.buildSessionFactory(HIBERNATE_CONFIG_FILE, User.class)
                ),
                logger
            );
    }

    private static UserService userService(UserDao dao) {
        UserService userService = new CommonUserService(dao, logger);

        userService.save(TEST_ADMIN);

        return userService;
    }
}
