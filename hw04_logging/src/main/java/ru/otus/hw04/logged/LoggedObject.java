package ru.otus.hw04.logged;

import java.util.List;

public class LoggedObject {
    @Log
    public void action(String param) {
        System.out.println("Action called");
    }

    @Log
    public int intsAction(List<Integer> ints, String test) {
        System.out.println("Action on list and string called");

        return 5;
    }

    @Log
    public int anotherAction(int testInt) {
        System.out.println("Another action called");

        return testInt + 3;
    }

    @Log
    public void stubAction(StubObject stub) {
        System.out.println("Action on stub called");
    }

    public void notLoggedAction() {
        System.out.println("Not logged action called");
    }
}
