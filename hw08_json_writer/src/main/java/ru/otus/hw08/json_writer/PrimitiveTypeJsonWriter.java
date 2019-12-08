package ru.otus.hw08.json_writer;

public class PrimitiveTypeJsonWriter implements JsonWriter{
    private final Object object;

    public PrimitiveTypeJsonWriter(Object object) {
        this.object = object;
    }

    @Override
    public String toJson() {
        return object.toString();
    }
}
