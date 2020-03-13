package ru.otus.hw16.db_service.repository;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.hw16.db_service.sessionmanager.SessionManager;
import ru.otus.hw16.db_service.sessionmanager.hibernate.HibernateDatabaseSession;
import ru.otus.hw16.db_service.sessionmanager.hibernate.HibernateSessionManager;
import ru.otus.hw16.lib.domain.User;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class HibernateUserRepository implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(HibernateUserRepository.class);

    private final HibernateSessionManager sessionManager;

    @Autowired
    public HibernateUserRepository(HibernateSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return
            Optional.ofNullable(
                (User)sessionManager
                    .getCurrentSession()
                    .getHibernateSession()
                    .createCriteria(User.class)
                    .add(Restrictions.eq("login", login))
                    .add(Restrictions.eq("password", password))
                    .uniqueResult()
            );
    }

    @Override
    public Optional<User> save(User user) {
        HibernateDatabaseSession currentSession = sessionManager.getCurrentSession();

        try {
            Session hibernateSession = currentSession.getHibernateSession();

            if (user.getId() > 0) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
            }

            return Optional.of(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        try {
            return
                sessionManager
                    .getCurrentSession()
                    .getHibernateSession()
                    .createQuery("select distinct u from ru.otus.hw16.lib.domain.User u", User.class)
                    .getResultList()
            ;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return Collections.emptyList();
    }

    @Override
    public SessionManager sessionManager() {
        return sessionManager;
    }
}
