package ru.otus.hw16.db_service.sessionmanager.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.otus.hw16.db_service.sessionmanager.DatabaseSession;

public class HibernateDatabaseSession implements DatabaseSession {
  private final Session session;
  private final Transaction transaction;

  HibernateDatabaseSession(Session session) {
    this.session = session;
    this.transaction = session.beginTransaction();
  }

  public Session getHibernateSession() {
    return session;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public void close() {
    if (transaction.isActive()) {
      transaction.commit();
    }
    session.close();
  }
}
