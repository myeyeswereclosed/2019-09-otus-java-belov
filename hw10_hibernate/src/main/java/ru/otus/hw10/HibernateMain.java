package ru.otus.hw10;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw10.api.entity.AddressDataSet;
import ru.otus.hw10.api.entity.PhoneDataSet;
import ru.otus.hw10.api.entity.User;
import ru.otus.hw10.api.service.DBServiceUser;
import ru.otus.hw10.api.service.DbServiceUserImpl;
import ru.otus.hw10.hibernate.HibernateUtils;
import ru.otus.hw10.hibernate.dao.UserDaoHibernate;
import ru.otus.hw10.hibernate.sessionmanager.SessionManagerHibernate;
import java.util.Optional;

public class HibernateMain {
    private static Logger logger = LoggerFactory.getLogger(HibernateMain.class);

    public static void main(String[] args) {
        DBServiceUser dbService = dbService();

        long id = dbService.saveUser(createUser());

        outputUserOptional("Created user", dbService.getUser(id));
    }

    private static DBServiceUser dbService() {
        SessionManagerHibernate sessionManager =
            new SessionManagerHibernate(
                HibernateUtils.buildSessionFactory("hibernate.cfg.xml", entityClasses())
            );

        return new DbServiceUserImpl(new UserDaoHibernate(sessionManager));
    }

    private static Class[] entityClasses() {
        return new Class[] {User.class, AddressDataSet.class, PhoneDataSet.class};
    }

    private static User createUser() {
        return
            new User("Вася", new AddressDataSet("Karla Marksa"))
                .addPhone(new PhoneDataSet("+79161112233"))
                .addPhone(new PhoneDataSet("+79175556677"))
                .addPhone(new PhoneDataSet("+74958888888"))
                .addPhone(new PhoneDataSet("+74991231231"))
        ;
    }

    private static void outputUserOptional(String header, Optional<User> mayBeUser) {
        System.out.println("-----------------------------------------------------------");
        System.out.println(header);
        mayBeUser.ifPresentOrElse(System.out::println, () -> logger.info("User not found"));
    }
}
