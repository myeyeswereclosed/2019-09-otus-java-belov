package ru.otus.hw08.json_writer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JsonWriterFactory {
    public static JsonWriter create(Object object) {
        if (object == null) {
            return new NullJsonWriter();
        }

        if (hasPrimitiveType(object)) {
            return new PrimitiveTypeJsonWriter(object);
        }

        if (isStringOrChar(object)) {
            return new StringOrCharJsonWriter(object);
        }

        if (isArray(object)) {
            return new ArrayJsonWriter(object);
        }

        if (isCollection(object)) {
            return new CollectionJsonWriter((Collection<?>) object);
        }

        return new ObjectJsonWriter(object);
    }


    private static boolean isCollection(Object object) {
        return object instanceof Collection;
    }

    private static boolean isArray(Object object) {
        return object.getClass().isArray();
    }

    private static boolean hasPrimitiveType(Object object) {
        return primitiveTypes().contains(object.getClass());
    }

    private static boolean isStringOrChar(Object object) {
        return object instanceof String || object instanceof Character;
    }

    private static List<Class<?>> primitiveTypes() {
        List<Class<?>> primitiveTypes = new ArrayList<>();

        primitiveTypes.add(Boolean.class);
        primitiveTypes.add(Byte.class);
        primitiveTypes.add(Short.class);
        primitiveTypes.add(Integer.class);
        primitiveTypes.add(Long.class);
        primitiveTypes.add(Float.class);
        primitiveTypes.add(Double.class);

        return primitiveTypes;
    }
}
