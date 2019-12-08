package ru.otus.hw08.json_writer;

import java.lang.reflect.Array;

public class ArrayJsonWriter extends BaseJsonWriter implements JsonWriter{
    private final Object object;

    public ArrayJsonWriter(Object object) {
        this.object = object;
    }

    @Override
    public String toJson() {
        var builder = new StringBuilder();

        int length = Array.getLength(object);

        for (int i = 0; i < length; i++) {
            builder
                .append(JsonWriterFactory.create(Array.get(object, i)).toJson())
                .append(',');
        }

        return result(builder.toString());
    }

    @Override
    char[] braces() {
        return new char[]{'[', ']'};
    }
}
