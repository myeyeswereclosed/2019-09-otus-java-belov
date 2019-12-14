package ru.otus.hw09;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw09.api.model.Account;
import ru.otus.hw09.api.model.User;
import ru.otus.hw09.api.sessionmanager.SessionManager;
import ru.otus.hw09.h2.DataSourceH2;
import ru.otus.hw09.jdbc.executor.DbExecutor;
import ru.otus.hw09.jdbc.executor.OrmDbExecutor;
import ru.otus.hw09.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.hw09.jdbc.template.OrmJdbcTemplate;

public class JdbcTemplateMain {
  private static Logger logger = LoggerFactory.getLogger(JdbcTemplateMain.class);

  public static void main(String[] args) {
    DataSource dataSource = new DataSourceH2();
    SessionManager sessionManager = new SessionManagerJdbc(dataSource);
    DbExecutor executor = new OrmDbExecutor<>(logger);

    tableCreationMap().forEach(
        (clazz, query ) -> {
            try {
                executor.createTable(dataSource.getConnection(), clazz, query);
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
    );

    OrmJdbcTemplate<User> userTemplate = new OrmJdbcTemplate<>(sessionManager, executor, logger);

    User user = new User(5, "Nikolay Ivanov", 35);

    userTemplate.create(user);
    userTemplate.update(user.becomeOlder(5));

    User userSaved = userTemplate.load(5, User.class);

    logger.info("User from db = " + userSaved);

    OrmJdbcTemplate<Account> accountTemplate = new OrmJdbcTemplate<>(sessionManager, executor, logger);

    Account account = new Account();

    account.setNo(2);
    account.setType("RUB");
    account.setRest(12.35);

    accountTemplate.create(account);
    accountTemplate.update(account.add(2.65));

    logger.info("Account from db = " + accountTemplate.load(2, Account.class));

    accountTemplate.createOrUpdate(account.add((float)5));

    logger.info("Account from db = " + accountTemplate.load(2, Account.class));

    userTemplate.createOrUpdate(new User(6, "Ivan Ivanov", 24));

    logger.info("New user = " + userTemplate.load(6, User.class));
  }

  private static Map<Class, String> tableCreationMap() {
      var result = new HashMap<Class, String>();

      result.put(
          User.class,
          "create table User(id bigint(20) not null auto_increment, name varchar(255), age int(3))"
      );
      result.put(
          Account.class,
          "create table Account(no bigint(20) not null auto_increment, type varchar(255), rest number)"
      );

      return result;
  }
}
