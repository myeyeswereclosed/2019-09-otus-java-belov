package ru.otus.hw05.tests;

import ru.otus.hw05.framework.annotation.After;
import ru.otus.hw05.framework.annotation.Before;
import ru.otus.hw05.framework.annotation.Test;

public class TestExample {
    @Before
    public void setUp() {
        System.out.println("Before invoked");
    }

    @After
    public void tearDown() {
        System.out.println("After invoked");
    }

    @Test
    public void first() {
        System.out.println("Test run successfully");
    }

    @Test
    public void fail() {
        throw new AssertionError("Test fail");
    }

}
