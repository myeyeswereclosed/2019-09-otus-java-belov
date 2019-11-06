package ru.otus.hw05.framework;

import ru.otus.hw05.framework.annotation.After;
import ru.otus.hw05.framework.annotation.Before;
import ru.otus.hw05.framework.annotation.Test;
import org.apache.commons.lang.exception.ExceptionUtils;
import ru.otus.hw05.framework.test_result.TestResult;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class TestRunner {
    private String className;

    private List<Method> beforeMethods = new ArrayList<>();
    private List<Method> afterMethods = new ArrayList<>();
    private List<Method> testMethods = new ArrayList<>();

    private Map<TestResult, Integer> statistics = new HashMap<>();

    public TestRunner(String className) {
        this.className = className;
    }

    public void run() {
        try {
            Class clazz = Class.forName(className);

            // in case of multiple @Test methods
            beforeMethods = findMethods(clazz, Before.class);
            afterMethods = findMethods(clazz, After.class);
            testMethods = findMethods(clazz, Test.class);

            runTests(clazz.newInstance());

            System.out.println(statisticsAsString());
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void runTests(Object test) {
        testMethods.forEach(testMethod -> runTestMethod(test, testMethod));
    }

    private void runTestMethod(Object test, Method method) {
        try {
            invoke(test, beforeMethods);

            runTest(test, method);

            invoke(test, afterMethods);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void runTest(Object test, Method method) {
        try {
            method.invoke(test);

            updateStatistics(TestResult.SUCCESS);
        } catch (InvocationTargetException e) {
            handleTestFail(e);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void handleTestFail(InvocationTargetException e) {
        Throwable cause = e.getCause();

        if (cause instanceof AssertionError) {
            handleAssertionError(e);
        } else {
            handleException(cause);
        }
    }

    private void handleAssertionError(InvocationTargetException e) {
        String[] traces = ExceptionUtils.getRootCauseStackTrace(e);

        System.out.println(traces[0] + traces[1]);

        updateStatistics(TestResult.FAIL);
    }

    private void handleException(Throwable e) {
        System.out.println("Exception occured. Trace:\r\n" + ExceptionUtils.getStackTrace(e));
        updateStatistics(TestResult.ERROR);
    }

    private void invoke(Object test, List<Method> methods) throws Exception {
        for (Method method : methods) {
            method.invoke(test);
        }
    }

    private  List<Method> findMethods(Class clazz, Class annotation) {
        return
            Arrays
                .stream(clazz.getMethods())
                .filter(method -> method.getAnnotation(annotation) != null)
                .collect(Collectors.toList())
        ;
    }

    private void updateStatistics(TestResult result) {
        int counter = testResultCounter(result);

        statistics.put(result, ++counter);
    }

    private String statisticsAsString() {
        return
            new StringBuilder()
                .append("Run test ").append(className).append("(tests count = ").append(testMethods.size()).append(')')
                .append(". Tests passed: ").append(testResultCounter(TestResult.SUCCESS))
                .append(". Tests failed: ").append(testResultCounter(TestResult.FAIL))
                .append(". Tests errors: ").append(testResultCounter(TestResult.ERROR))
                    .toString()
            ;
    }

    private int testResultCounter(TestResult result) {
        return Optional.ofNullable(statistics.get(result)).orElse(0);
    }
}
