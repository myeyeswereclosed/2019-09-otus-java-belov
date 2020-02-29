package ru.otus.hw14.counter;

import ru.otus.hw14.NumSequence;

public class Counter {
    private final static String INCREMENT = "inc";
    private final static String DECREMENT = "dec";

    private final int min;
    private final int max;
    private final int sleepInterval;

    private int value;
    private String operation = INCREMENT;
    private String expected = NumSequence.FIRST_NAME;

    public Counter(int min, int max, int sleepInterval) {
        this.min = min;
        this.max = max;
        value = min;

        this.sleepInterval = sleepInterval;
    }


    public void print() throws InterruptedException {
        synchronized (this) {
            if (!Thread.currentThread().getName().equals(expected)) {
                wait();
            }

            System.out.println(Thread.currentThread().getName() + ":" + value);
            changeExpectedRunner();
            Thread.sleep(sleepInterval);
            notifyAll();
        }
    }

    public void printChanged() throws InterruptedException {
        synchronized (this) {
            print();
            changeValue();
        }
    }

    public Counter changeValue() {
        changeOperationIfNeeded();

        if (operation.equals(DECREMENT)) {
            value--;
        }

        if (operation.equals(INCREMENT)) {
            value++;
        }

        return this;
    }

    private void changeExpectedRunner() {
        if (expected.equals(NumSequence.FIRST_NAME)) {
            expected = NumSequence.SECOND_NAME;
        } else {
            expected = NumSequence.FIRST_NAME;
        }
    }

    private void changeOperationIfNeeded() {
        if (value == max) {
            operation = DECREMENT;
        }

        if (value == min) {
            operation = INCREMENT;
        }
    }
}
