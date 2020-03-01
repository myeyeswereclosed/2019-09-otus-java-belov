package ru.otus.hw14;

import ru.otus.hw14.counter.Counter;
import ru.otus.hw14.threads.BaseRunner;
import ru.otus.hw14.threads.Modifier;
import ru.otus.hw14.threads.Printer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NumSequence {
    public static final String FIRST_NAME = "First";
    public static final String SECOND_NAME = "Second";

    public static void main(String[] args) {
        var counter = new Counter(1, 10, 400);

        var first = new Printer(counter, FIRST_NAME);
        var second = new Modifier(counter, SECOND_NAME);

        var runners = Arrays.asList(first, second);
        Collections.shuffle(runners);

        runners.forEach(BaseRunner::start);
    }
}
