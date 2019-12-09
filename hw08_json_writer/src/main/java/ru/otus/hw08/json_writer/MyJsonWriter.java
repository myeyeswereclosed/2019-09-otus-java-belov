package ru.otus.hw08.json_writer;

public class MyJsonWriter {
    public String asJsonString(Object object) {
        return JsonWriterFactory.create(object).toJson();
    }
}
