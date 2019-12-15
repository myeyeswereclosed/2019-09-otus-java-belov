package ru.otus.hw09.jdbc.template;

import org.slf4j.Logger;
import ru.otus.hw09.api.model.Id;
import ru.otus.hw09.api.sessionmanager.SessionManager;
import ru.otus.hw09.jdbc.mapper.SqlTypeMapper;
import ru.otus.hw09.jdbc.executor.DbExecutor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class OrmJdbcTemplate<T> implements JdbcTemplate {
    private static final List<Class> validIdClasses = validIdClasses();

    private final SessionManager sessionManager;
    private final DbExecutor dbExecutor;
    private final Logger logger;

    public OrmJdbcTemplate(SessionManager sessionManager, DbExecutor dbExecutor, Logger logger) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.logger = logger;
    }

    @Override
    public void create(Object objectData) {
        if (!isDbModel(objectData.getClass())) {
            return;
        }

        Map<String, Object> queryMap = fieldNameToValueMap(objectData);

        createOrUpdateDbRow(
            makeCreateQuery(queryMap, objectData),
            new ArrayList<>(queryMap.values())
        );
    }

    private void createOrUpdateDbRow(String query, List<Object> values) {
        logger.info("Passing query = '" + query + "' to DbExecutor " + values);

        sessionManager.beginSession();

        try {
            dbExecutor.upsert(sessionManager.getCurrentSession().getConnection(), query, values);
            sessionManager.commitSession();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);

            sessionManager.rollbackSession();
        }
    }

    private String tableName(Object object) {
        return object.getClass().getSimpleName();
    }

    private String tableName(Class clazz) {
        return clazz.getSimpleName();
    }

    private String makeCreateQuery(Map<String, Object> queryMap, Object object) {
        StringBuilder queryBuilder = new StringBuilder("insert into ").append(tableName(object)).append("(");

        return
            queryBuilder
                .append(String.join(",", queryMap.keySet())).append(") values(")
                .append(queryMap.values().stream().map(value -> "?").collect(Collectors.joining(","))).append(")")
                .toString()
            ;
    }

    @Override
    public void update(Object objectData) {
        if (!isDbModel(objectData.getClass())) {
            return;
        }

        Map<String, Object> queryMap = fieldNameToValueMap(objectData);

        var values = new ArrayList<>(queryMap.values());
        var sqlValues = values.subList(1, values.size());
        sqlValues.add(values.get(0));

        createOrUpdateDbRow(makeUpdateQuery(queryMap, objectData), sqlValues);
    }

    @Override
    public void createOrUpdate(Object objectData) {
        if (!isDbModel(objectData.getClass())) {
            return;
        }

        logger.info("Creating or updating " + objectData);

        if (load(idFieldValue(objectData), objectData.getClass()) == null) {
            create(objectData);
        } else {
            update(objectData);
        }
    }

    private String makeUpdateQuery(Map<String, Object> queryMap, Object object) {
        return
            new StringBuilder("update ")
                .append(tableName(object))
                .append(" set ")
                .append(
                    queryMap.keySet()
                        .stream()
                        .skip(1)
                        .map(col -> col + " = ?")
                        .collect(Collectors.joining(","))
                )
                .append(" where " + idField(object.getClass()) + " = ?")
                .toString()
            ;
    }

    @Override
    public T load(long id, Class clazz) {
        if (!isDbModel(clazz)) {
            return null;
        }

        var objectFields = nonStaticFields(clazz);
        var query = makeSelectQuery(objectFields, clazz);

        var selectResult = Optional.empty();

        logger.info("Trying to find object of '" + clazz.getSimpleName() + "' : " + query + " [" + id + ']');

        sessionManager.beginSession();

        try {
            selectResult =
                dbExecutor.selectRecord(
                    sessionManager.getCurrentSession().getConnection(),
                    query,
                    id,
                    resultSet -> handleResultSet((ResultSet)resultSet, clazz, objectFields)
                );

                sessionManager.commitSession();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);

                sessionManager.rollbackSession();
            }

        return (T)selectResult.orElse(null);
    }

    private T handleResultSet(ResultSet resultSet, Class<T> clazz, List<Field> objectFields) {
        try {
            var result = clazz.newInstance();

            Field[] fields = new Field[objectFields.size()];
            fields = objectFields.toArray(fields);
            int fieldsSize = fields.length;

            if (resultSet.next()) {
                for (int i = 0; i < fieldsSize; i++) {
                    SqlTypeMapper.mapFromSql(resultSet, i + 1, result, fields[i]);
                }

                return result;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    private String makeSelectQuery(List<Field> fields, Class clazz) {
        return
            new StringBuilder("select ")
                .append(
                    fields
                        .stream()
                        .map(Field::getName)
                        .collect(Collectors.joining(","))
                )
                .append(" from ")
                .append(tableName(clazz))
                .append(" where " + idField(clazz) + " = ?")
                    .toString()
            ;
    }

    private boolean isDbModel(Class clazz) {
        List<Field> ids = idFields(clazz);

        if (ids.size() == 1 && validIdClasses.contains(ids.get(0).getType())) {
            return true;
        }

        logger.error(clazz.getSimpleName() + " is not valid db model. Check it has one @Id-annotated field");

        return false;
    }

    private List<Field> idFields(Class clazz) {
        return
            Arrays
                .stream(clazz.getDeclaredFields())
                .filter(this::isIdField)
                .collect(Collectors.toList())
        ;
    }

    private String idField(Class clazz) {
        return idFields(clazz).get(0).getName();
    }

    private long idFieldValue(Object object) {
        return (Long)fieldValue(idFields(object.getClass()).get(0), object).get();
    }

    private boolean isIdField(Field field) {
        return field.isAnnotationPresent(Id.class);
    }

    private Map<String, Object> fieldNameToValueMap(Object object) {
        var result = new LinkedHashMap<String, Object>();

        nonStaticFields(object.getClass())
            .forEach(
                field -> fieldValue(field, object).map(
                    value -> result.put(field.getName(), value)
                )
            );

        return result;
    }

    private List<Field> nonStaticFields(Class clazz) {
        return
            Arrays
                .stream(clazz.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList())
        ;
    }

    private Optional<Object> fieldValue(Field field, Object object) {
        var accessible = field.isAccessible();
        Optional<Object> result = Optional.empty();

        field.setAccessible(true);

        try {
            result = Optional.ofNullable(field.get(object));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        field.setAccessible(accessible);

        return result;
    }

    private static List<Class> validIdClasses() {
        var result = new ArrayList<Class>();

        result.add(Integer.class);
        result.add(int.class);
        result.add(Long.class);
        result.add(long.class);

        return result;
    }
}
