package ru.otus.hw04;

import ru.otus.hw04.logged.LoggedObject;
import ru.otus.hw04.logged.StubObject;

import java.util.ArrayList;

public class LogMain {
//    -javaagent:hw04_logging.jar -jar hw04_logging.jar
    public static void main(String[] args) throws Exception {
        LoggedObject testObject = new LoggedObject();

        testObject.action("123");

        System.out.println(testObject.anotherAction(5));

        testObject.intsAction(new ArrayList<>() {{ add(1); add(2); }}, "some string");

        testObject.stubAction(new StubObject());

        testObject.notLoggedAction();
    }
}
