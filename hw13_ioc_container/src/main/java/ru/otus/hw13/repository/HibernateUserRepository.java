package ru.otus.hw13.repository;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.hw13.api.sessionmanager.SessionManager;
import ru.otus.hw13.api.sessionmanager.hibernate.HibernateDatabaseSession;
import ru.otus.hw13.api.sessionmanager.hibernate.HibernateSessionManager;
import ru.otus.hw13.domain.User;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class HibernateUserRepository implements UserRepository {
    private final HibernateSessionManager sessionManager;
    private final Logger logger;

    @Autowired
    public HibernateUserRepository(HibernateSessionManager sessionManager, Logger logger) {
        this.sessionManager = sessionManager;
        this.logger = logger;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return
            Optional.of(
                (User)sessionManager
                    .getCurrentSession()
                    .getHibernateSession()
                    .createCriteria(User.class)
                    .add(Restrictions.eq("login", login))
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
                    .createQuery("select u from User u", User.class)
                    .getResultList();
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
