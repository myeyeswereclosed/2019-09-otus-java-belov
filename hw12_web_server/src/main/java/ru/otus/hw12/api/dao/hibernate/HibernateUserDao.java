package ru.otus.hw12.api.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import ru.otus.hw12.api.dao.UserDaoException;
import ru.otus.hw12.api.dao.UserDao;
import ru.otus.hw12.api.sessionmanager.SessionManager;
import ru.otus.hw12.api.sessionmanager.hibernate.DatabaseSessionHibernate;
import ru.otus.hw12.api.sessionmanager.hibernate.SessionManagerHibernate;
import ru.otus.hw12.model.User;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HibernateUserDao implements UserDao {
  private final SessionManagerHibernate sessionManager;
  private final Logger logger;

  public HibernateUserDao(SessionManagerHibernate sessionManager, Logger logger) {
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
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
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

      throw new UserDaoException(e);
    }
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
  public Optional<SessionManager> sessionManager() {
    return Optional.of(sessionManager);
  }
}
