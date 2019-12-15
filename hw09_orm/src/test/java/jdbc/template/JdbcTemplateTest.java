package jdbc.template;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw09.JdbcTemplateMain;
import ru.otus.hw09.api.model.Account;
import ru.otus.hw09.api.model.Id;
import ru.otus.hw09.api.sessionmanager.SessionManager;
import ru.otus.hw09.h2.DataSourceH2;
import ru.otus.hw09.jdbc.executor.DbExecutor;
import ru.otus.hw09.jdbc.executor.OrmDbExecutor;
import ru.otus.hw09.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.hw09.jdbc.template.OrmJdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.fail;

public class JdbcTemplateTest {
    private static int accountId = 2;
    private static double sum = 100.45;
    private static double withdrawalSum = 5.45;
    private static String accountType = "EUR";

    private static Logger logger = LoggerFactory.getLogger(JdbcTemplateMain.class);

    private DataSource dataSource;
    private SessionManager sessionManager;

    @Test
    public void noId() {
        var template = new OrmJdbcTemplate<WithoutId>(sessionManager, new OrmDbExecutor<>(logger), logger);

        template.create(new WithoutId().setId(1));

        Assert.assertNull(template.load(1, WithoutId.class));
    }

    @Test
    public void tooManyIds() {
        var template = new OrmJdbcTemplate<TooManyIds>(sessionManager, new OrmDbExecutor<>(logger), logger);

        template.create(new TooManyIds().setId(1));

        Assert.assertNull(template.load(1, TooManyIds.class));
    }

    @Test
    public void create() {
        try {
            dataSource = new DataSourceH2();
            var executor = new OrmDbExecutor<>(logger);
            createTable(executor);

            var template = new OrmJdbcTemplate<Account>(new SessionManagerJdbc(dataSource), executor, logger);

            template.create(createAccount());

            assertAccountStored(sum);

            dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    private void createTable(DbExecutor executor) throws SQLException {
        executor.createTable(
            dataSource.getConnection(),
            Account.class,
            "create table Account(no bigint(20) not null auto_increment, type varchar(255), rest number)"
        );
    }

    private void dropTable() throws SQLException {
        try (PreparedStatement pst = dataSource.getConnection().prepareStatement(" drop table account;")) {
            pst.executeUpdate();
        }
    }

    private void assertAccountStored(double expectedSum) throws SQLException {
        ResultSet resultSet;

        try (PreparedStatement pst = dataSource.getConnection().prepareStatement(" select * from account;")) {
            resultSet = pst.executeQuery();

            resultSet.next();

            Assert.assertEquals(accountId, resultSet.getLong("no"));
            Assert.assertEquals(expectedSum, resultSet.getDouble("rest"), 0.00001);
            Assert.assertEquals(accountType, resultSet.getString("type"));
        }
    }

    private Account createAccount() {
        Account account = new Account();

        account.setNo(accountId);
        account.setType(accountType);
        account.setRest(sum);

        return account;
    }

    @Test
    public void update() {
        try {
            dataSource = new DataSourceH2();
            var executor = new OrmDbExecutor<>(logger);
            createTable(executor);

            var template = new OrmJdbcTemplate<Account>(new SessionManagerJdbc(dataSource), executor, logger);
            var account = createAccount();

            template.create(account);

            assertAccountStored(sum);

            template.update(account.withdraw(withdrawalSum));

            assertAccountStored(sum - withdrawalSum);

            dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void load() {
        try {
            dataSource = new DataSourceH2();
            var executor = new OrmDbExecutor<>(logger);
            createTable(executor);

            var template = new OrmJdbcTemplate<Account>(new SessionManagerJdbc(dataSource), executor, logger);
            var account = createAccount();

            template.createOrUpdate(account);

            assertLoaded(template.load(accountId, Account.class));

            dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    private void assertLoaded(Account account) {
        Assert.assertNotNull(account);
        Assert.assertEquals(accountId, account.getNo());
        Assert.assertEquals(sum, account.getRest(), 0.000001);
        Assert.assertEquals(accountType, account.getType());
    }

    private class WithoutId {
        private int id;
        private int num = 5;
        private String message = "My message";

        public WithoutId setId(int id) {
            this.id = id;

            return this;
        }
    }

    private class TooManyIds {
        @Id
        private int id;

        @Id
        private int num = 5;
        private String message = "My message";

        public TooManyIds setId(int id) {
            this.id = id;

            return this;
        }
    }
}


