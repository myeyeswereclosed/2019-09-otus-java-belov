package ru.otus.hw05.framework;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class TestFramework {
    public static void run() {
        // passing all test classes from package "ru.otus.hw05.tests" to test runner
        makeReflections().getSubTypesOf(Object.class).forEach(
            test -> {
                System.out.println(test);
                new TestRunner(test.getName()).run();
            }
        );
    }

    private static Reflections makeReflections() {
        return
            new Reflections(
                new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage("ru.otus.hw05"))
                    .setScanners(new SubTypesScanner(false))
                    .filterInputsBy(new FilterBuilder().includePackage("ru.otus.hw05.tests"))
            );
    }
}
