package ru.otus.hw09.jdbc.executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface DbExecutor<T> {
    void createTable(Connection connection, Class clazz, String query) throws SQLException;

    void upsert(Connection connection, String sql, List<String> params) throws SQLException;

    Optional<T> selectRecord(
        Connection connection,
        String sql,
        long id,
        Function<ResultSet, T> rsHandler
    ) throws SQLException;
}
