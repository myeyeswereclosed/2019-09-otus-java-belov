package ru.otus.hw14.threads;

import ru.otus.hw14.counter.Counter;

public class Modifier extends BaseRunner {
    public Modifier(Counter counter, String name) {
        super(counter, name);
    }

    @Override
    public void runCounter() throws InterruptedException {
        counter.printChanged();
    }
}
