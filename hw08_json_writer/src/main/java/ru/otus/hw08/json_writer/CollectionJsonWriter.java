package ru.otus.hw08.json_writer;

import java.util.Collection;

public class CollectionJsonWriter extends BaseJsonWriter implements JsonWriter {
    private final Collection<?> collection;

    public CollectionJsonWriter(Collection<?> collection) {
        this.collection = collection;
    }

    @Override
    public String toJson() {
        var builder = new StringBuilder();

        collection.forEach(
            item -> builder
                .append(JsonWriterFactory.create(item).toJson())
                .append(',')
        );

        return result(builder.toString());
    }

    @Override
    char[] braces() {
        return new char[]{'[', ']'};
    }
}
