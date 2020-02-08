package ru.otus.hw14.threads;

import ru.otus.hw14.counter.Counter;

public class Printer extends BaseRunner {
    public Printer(Counter counter, String name) {
       super(counter, name);
    }

    @Override
    public void runCounter() throws InterruptedException {
        counter.print();
    }
}
