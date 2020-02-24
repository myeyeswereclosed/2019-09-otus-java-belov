package ru.otus.hw14.threads;

import ru.otus.hw14.counter.Counter;

abstract public class BaseRunner extends Thread {
    private static final int LIMIT = 100;
    private int iteration;
    protected Counter counter;

    public BaseRunner(Counter counter, String name) {
        this.setName(name);
        this.counter = counter;
    }

    @Override
    public void run() {
        while (iteration < LIMIT) {
            try {
                runCounter();
                iteration++;
            } catch (InterruptedException e) {
                System.out.println("Interruption exception occured");
                interrupt();
            }
        }
    }

    abstract protected void runCounter() throws InterruptedException;
}
