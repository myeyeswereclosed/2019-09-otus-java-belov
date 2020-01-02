package ru.otus.hw10.dao;

import org.assertj.core.api.Condition;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw10.api.entity.AddressDataSet;
import ru.otus.hw10.api.entity.PhoneDataSet;
import ru.otus.hw10.api.entity.User;
import ru.otus.hw10.hibernate.HibernateUtils;
import ru.otus.hw10.hibernate.dao.UserDaoHibernate;
import ru.otus.hw10.hibernate.sessionmanager.SessionManagerHibernate;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с пользователями должно ")
class UserDaoHibernateTest {
  private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate-test.cfg.xml";

  private static final String TEST_USER_NAME = "Вася";
  private static final String TEST_USER_ADDRESS = "Some street";
  private static final String PHONE_1 = "1234";
  private static final String PHONE_2 = "5678";

  private SessionFactory sessionFactory;
  private SessionManagerHibernate sessionManager;
  private UserDaoHibernate userDao;

  @BeforeEach
  public void setUp() {
    sessionFactory =
        HibernateUtils.buildSessionFactory(
            HIBERNATE_CFG_XML_FILE_RESOURCE,
            User.class,
            AddressDataSet.class,
            PhoneDataSet.class
        );
    sessionManager = new SessionManagerHibernate(sessionFactory);
    userDao = new UserDaoHibernate(sessionManager);
  }

  @AfterEach
  void tearDown() {
    sessionFactory.close();
  }

  @DisplayName(" корректно сохранять и загружать пользователя без телефона ")
  @Test
  void shouldCorrectlySaveUser() {
    User expectedUser = createUser();
    sessionManager.beginSession();
    long id = userDao.saveUser(expectedUser);
    sessionManager.commitSession();

    assertThat(id).isGreaterThan(0);

    User actualUser = loadUser(id);

    assertThat(actualUser)
        .isNotNull()
        .hasFieldOrPropertyWithValue("name", TEST_USER_NAME)
        .has(new Condition<>(user -> user.livesAt(TEST_USER_ADDRESS), "", ""))
    ;
  }

  @DisplayName(" корректно сохранять и загружать пользователя с его телефонными номерами ")
  @Test
  void shouldCorrectlySaveUserAndHisPhones() {
    User expectedUser = createUserWithPhones();
    sessionManager.beginSession();
    long id = userDao.saveUser(expectedUser);
    sessionManager.commitSession();

    assertThat(id).isGreaterThan(0);

    User actualUser = loadUser(id);

    assertThat(actualUser)
        .isNotNull()
        .hasFieldOrPropertyWithValue("name", TEST_USER_NAME)
        .has(new Condition<>(user -> user.livesAt(TEST_USER_ADDRESS), "", ""))
        .has(new Condition<>(user -> user.hasPhone(PHONE_1) && user.hasPhone(PHONE_2), "", ""))
    ;
  }

  @DisplayName(" возвращать менеджер сессий")
  @Test
  void getSessionManager() {
    assertThat(userDao.getSessionManager()).isNotNull().isEqualTo(sessionManager);
  }

  private User loadUser(long id) {
    try (Session session = sessionFactory.openSession()) {
      User user =  session.find(User.class, id);

      Hibernate.initialize(user.getPhones());

      return user;
    }
  }

  private User createUser() {
    return new User(TEST_USER_NAME, new AddressDataSet(TEST_USER_ADDRESS));
  }

  private User createUserWithPhones() {
    return
        new User(TEST_USER_NAME, new AddressDataSet(TEST_USER_ADDRESS))
          .addPhone(new PhoneDataSet(PHONE_1))
          .addPhone(new PhoneDataSet(PHONE_2))
    ;
  }
}
