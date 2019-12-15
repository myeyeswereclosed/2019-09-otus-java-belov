package ru.otus.hw09.jdbc.mapper;

import java.sql.SQLException;

public class SqlTypeMappingException extends SQLException {
   public SqlTypeMappingException(String reason) {
       super(reason);
   }
}
