package ru.otus.hw02;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DIYArrayListTest {
    private static Integer TEST_INT = 5;
    @Test
    public void add() {
        List<Integer> integerList = new DIYArrayList<>();

        assertAddPreconditions(integerList);

        integerList.add(TEST_INT);

        assertElementAdded(integerList);
    }

    private void assertAddPreconditions(List<Integer> testList) {
        Assert.assertTrue(testList.isEmpty());
    }

    private void assertElementAdded(List<Integer> integerList) {
        Assert.assertFalse(integerList.isEmpty());
        Assert.assertEquals(1, integerList.size());
        Assert.assertTrue(integerList.contains(TEST_INT));
        Assert.assertTrue(integerList.contains(5));
    }

    @Test
    public void contains() {
        List<Integer> diyList = new DIYArrayList<>();

        diyList.add(1);
        diyList.add(null);
        diyList.add(TEST_INT);

        assertContains(diyList);
    }

    private void assertContains(List<Integer> testList) {
        Assert.assertTrue(testList.contains(null));
        Assert.assertTrue(testList.contains(TEST_INT));
    }

    @Test
    public void addAll() {
        List<Integer> diyList = new DIYArrayList<>();

        diyList.add(TEST_INT);

        assertElementAdded(diyList);

        Collection<Integer> integersToAdd = intCollection();

        diyList.addAll(integersToAdd);

        assertAllAdded(diyList, integersToAdd);
    }

    private void assertAllAdded(List<Integer> testList, Collection<Integer> integersToAdd) {
        integersToAdd.forEach(value -> Assert.assertTrue(testList.contains(value)));
        Assert.assertEquals(5, testList.size());
    }

    private Collection<Integer> intCollection() {
        List<Integer> integers = new ArrayList<>();

        integers.add(4);
        integers.add(3);
        integers.add(2);
        integers.add(1);

        return integers;
    }
}
