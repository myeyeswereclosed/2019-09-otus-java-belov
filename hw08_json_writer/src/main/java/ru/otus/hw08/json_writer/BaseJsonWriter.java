package ru.otus.hw08.json_writer;

abstract public class BaseJsonWriter {
    protected String result(String initial) {
        var result =
            initial.isEmpty()
                ? initial
                : initial.substring(0, initial.length() - 1)
            ;

        var braces = braces();

        return braces[0] + result + braces[1];
    }

    abstract char[] braces();
}
