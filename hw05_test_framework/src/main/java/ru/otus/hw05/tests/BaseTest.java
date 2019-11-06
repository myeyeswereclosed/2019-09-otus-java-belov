package ru.otus.hw05.tests;

import ru.otus.hw05.framework.annotation.After;
import ru.otus.hw05.framework.annotation.Before;
import ru.otus.hw05.framework.annotation.Test;

public class BaseTest {
    @Before
    public void setUp() {
        System.out.println("Base test before invoked");
    }

    @After
    public void tearDown() {
        System.out.println("Base test after invoked");
    }

    @Test
    public void base() {
        System.out.println("Base test run successfully");
    }
}
