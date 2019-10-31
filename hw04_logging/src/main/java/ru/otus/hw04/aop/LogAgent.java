package ru.otus.hw04.aop;

import java.lang.instrument.Instrumentation;

public class LogAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new LogClassFileTransformer());
    }
}
