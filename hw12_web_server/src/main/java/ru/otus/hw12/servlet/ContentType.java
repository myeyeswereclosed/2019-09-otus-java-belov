package ru.otus.hw12.servlet;

public enum ContentType {
        APPLICATION_JSON("application/json;charset=UTF-8"),
        TEXT_HTML("text/html")
    ;

    private String name;

    ContentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
