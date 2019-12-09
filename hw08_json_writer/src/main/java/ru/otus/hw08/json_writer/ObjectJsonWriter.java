package ru.otus.hw08.json_writer;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ObjectJsonWriter extends BaseJsonWriter implements JsonWriter {
    private final Object object;

    public ObjectJsonWriter(Object object) {
        this.object = object;
    }

    @Override
    public String toJson() {
        var builder = new StringBuilder();

        List<Field> fields =
            Arrays
                .stream(object.getClass().getDeclaredFields())
                .filter(this::isForJsonSerialization)
                .collect(Collectors.toList());

        for (Field field : fields) {
            handleField(field, builder);
        }

        return result(builder.toString());
    }

    private void handleField(Field field, StringBuilder builder) {
        try {
            var accessible = field.isAccessible();

            field.setAccessible(true);
            var value = field.get(object);

            if (!Objects.isNull(value))
                builder
                    .append(new StringOrCharJsonWriter(field.getName()).toJson())
                    .append(':')
                    .append(JsonWriterFactory.create(value).toJson())
                    .append(',')
                ;

            field.setAccessible(accessible);
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    private boolean isForJsonSerialization(Field field) {
        return
            !Modifier.isStatic(field.getModifiers())
                &&
            !Modifier.isTransient(field.getModifiers())
        ;
    }

    @Override
    char[] braces() {
        return new char[]{'{', '}'};
    }
}
