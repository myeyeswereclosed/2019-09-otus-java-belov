package ru.otus.hw09.jdbc.mapper;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw09.JdbcTemplateMain;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SqlTypeMapper {
    private static Map<Class, String> classToStatementMethodMap = classToStatementMethodMap();
    private static Map<Class, String> classToResultSetMethodMap = classToResultSetMethodMap();

    private static Logger logger = LoggerFactory.getLogger(JdbcTemplateMain.class);

    public static void mapToSql(PreparedStatement statement, int index, Object value) throws SQLException {
        Class clazz = value.getClass();
        var method = classToStatementMethodMap.get(clazz);

        if (Objects.nonNull(method)) {
            try {
                statement
                    .getClass()
                    .getDeclaredMethod(method, int.class, resolveValueClass(clazz))
                    .invoke(statement, index, value);
                return;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        throw new SqlTypeMappingException("Failed to map object of " + value.getClass() + " to JDBC Statement type");
    }

    private static Class resolveValueClass(Class clazz) {
        return
            ClassUtils.isPrimitiveWrapper(clazz)
                ? ClassUtils.wrapperToPrimitive(clazz)
                : clazz
        ;
    }

    private static Map<Class, String> classToStatementMethodMap() {
        var map = new HashMap<Class, String>();

        map.put(String.class, "setString");
        map.put(Integer.class, "setInt");
        map.put(int.class, "setInt");
        map.put(Long.class, "setLong");
        map.put(long.class, "setLong");
        map.put(Boolean.class, "setBoolean");
        map.put(boolean.class, "setBoolean");
        map.put(Character.class, "setString");
        map.put(char.class, "setString");
        map.put(Double.class, "setDouble");
        map.put(double.class, "setDouble");
        map.put(Float.class, "setFloat");
        map.put(float.class, "setFloat");
        map.put(Date.class, "setDate");
        map.put(Array.class, "setArray");

        // etc

        return map;
    }

    public static void mapFromSql(ResultSet resultSet, int index, Object object, Field field) throws SQLException {
        var accessible = field.isAccessible();
        var method = classToResultSetMethodMap.get(field.getType());

        if (Objects.nonNull(method)) {
            field.setAccessible(true);

            try {
                var value =
                    resultSet
                        .getClass()
                        .getDeclaredMethod(method, int.class)
                        .invoke(resultSet, index)
                    ;

                field.set(object, value);

                field.setAccessible(accessible);

                return;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            field.setAccessible(accessible);
        }

        throw new SqlTypeMappingException(
            "Failed to make object of " + object.getClass().getSimpleName() + " to JDBC type"
        );
    }

    private static Map<Class, String> classToResultSetMethodMap() {
        Map<Class, String> map = new HashMap<>();

        map.put(String.class, "getString");
        map.put(Integer.class, "getInt");
        map.put(int.class, "getInt");
        map.put(Long.class, "getLong");
        map.put(long.class, "getLong");
        map.put(Boolean.class, "getBoolean");
        map.put(boolean.class, "getBoolean");
        map.put(Character.class, "getString");
        map.put(char.class, "getString");
        map.put(Double.class, "getDouble");
        map.put(double.class, "getDouble");
        map.put(Float.class, "getFloat");
        map.put(float.class, "getFloat");
        map.put(Date.class, "getDate");
        map.put(Array.class, "getArray");

        // etc

        return map;
    }
}
