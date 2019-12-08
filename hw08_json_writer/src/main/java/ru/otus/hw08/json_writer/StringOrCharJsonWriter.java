package ru.otus.hw08.json_writer;

import org.apache.commons.lang.StringEscapeUtils;

public class StringOrCharJsonWriter implements JsonWriter {
    private final Object object;

    public StringOrCharJsonWriter(Object object) {
        this.object = object;
    }

    @Override
    public String toJson() {
        return quoted(object.toString());
    }

    private String quoted(String value) {
        return
            new StringBuilder()
                .append('\"')
                .append(StringEscapeUtils.escapeJava(value))
                .append('\"')
                .toString()
        ;
    }
}
