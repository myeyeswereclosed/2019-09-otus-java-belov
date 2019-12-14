package ru.otus.hw09.jdbc.executor;

import org.slf4j.Logger;
import ru.otus.hw09.jdbc.mapper.SqlTypeMapper;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class OrmDbExecutor<T> implements DbExecutor<T> {
    private Logger logger;

    public OrmDbExecutor(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void createTable(Connection connection, Class clazz, String query) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.executeUpdate();
        }
        logger.info("Table " + clazz.getSimpleName() + " was created");
    }

    @Override
    public void upsert(Connection connection, String sql, List<String> params) throws SQLException {
        Savepoint savePoint = connection.setSavepoint("savePointName");
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            for (int idx = 0; idx < params.size(); idx++) {
                SqlTypeMapper.mapToSql(pst, idx + 1, params.get(idx));
            }
            pst.executeUpdate();

            logger.info("Model successfully stored");
        } catch (SQLException ex) {
            connection.rollback(savePoint);

            logger.error(ex.getMessage(), ex);

            throw ex;
        }
    }

    @Override
    public Optional<T> selectRecord(
        Connection connection,
        String sql,
        long id,
        Function<ResultSet, T> rsHandler
    ) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setLong(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }
}
