package ru.otus.hw05.tests;

import ru.otus.hw05.framework.annotation.Test;
import ru.otus.hw05.stub.CalcStub;

public class InheritedTest extends BaseTest {
    @Test
    public void stub() {
        new CalcStub().calc(1);
        System.out.println("Stub test success");
    }

    @Test
    public void stubError() {
        new CalcStub().calc(0);
    }
}
