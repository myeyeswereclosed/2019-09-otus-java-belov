package ru.otus.hw13.api.sessionmanager.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.hw13.api.sessionmanager.SessionManager;
import ru.otus.hw13.api.sessionmanager.SessionManagerException;

@Component
public class HibernateSessionManager implements SessionManager {
  private HibernateDatabaseSession databaseSession;
  private final SessionFactory sessionFactory;

  @Autowired
  public HibernateSessionManager(SessionFactory sessionFactory) {
    if (sessionFactory == null) {
      throw new SessionManagerException("SessionFactory is null");
    }
    this.sessionFactory = sessionFactory;
  }

  @Override
  public void beginSession() {
    try {
      databaseSession = new HibernateDatabaseSession(sessionFactory.openSession());
    } catch (Exception e) {
      throw new SessionManagerException(e);
    }
  }

  @Override
  public void commitSession() {
    checkSessionAndTransaction();
    try {
      databaseSession.getTransaction().commit();
      databaseSession.getHibernateSession().close();
    } catch (Exception e) {
      throw new SessionManagerException(e);
    }
  }

  @Override
  public void rollbackSession() {
    checkSessionAndTransaction();
    try {
      databaseSession.getTransaction().rollback();
      databaseSession.getHibernateSession().close();
    } catch (Exception e) {
      throw new SessionManagerException(e);
    }
  }

  @Override
  public void close() {
    if (databaseSession == null) {
      return;
    }
    Session session = databaseSession.getHibernateSession();
    if (session == null || !session.isConnected()) {
      return;
    }

    Transaction transaction = databaseSession.getTransaction();
    if (transaction == null || !transaction.isActive()) {
      return;
    }

    try {
      databaseSession.close();
      databaseSession = null;
    } catch (Exception e) {
      throw new SessionManagerException(e);
    }
  }

  @Override
  public HibernateDatabaseSession getCurrentSession() {
    checkSessionAndTransaction();

    return databaseSession;
  }

  private void checkSessionAndTransaction() {
    if (databaseSession == null) {
      throw new SessionManagerException("DatabaseSession not opened ");
    }
    Session session = databaseSession.getHibernateSession();
    if (session == null || !session.isConnected()) {
      throw new SessionManagerException("Session not opened ");
    }

    Transaction transaction = databaseSession.getTransaction();
    if (transaction == null || !transaction.isActive()) {
      throw new SessionManagerException("Transaction not opened ");
    }
  }
}
