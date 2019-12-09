package ru.otus.hw08.json_writer;

public class NullJsonWriter implements JsonWriter {
    @Override
    public String toJson() {
        return "null";
    }
}
