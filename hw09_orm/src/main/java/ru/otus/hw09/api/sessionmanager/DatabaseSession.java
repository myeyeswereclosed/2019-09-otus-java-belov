package ru.otus.hw09.api.sessionmanager;

import java.sql.Connection;

public interface DatabaseSession {
    Connection getConnection();
}
