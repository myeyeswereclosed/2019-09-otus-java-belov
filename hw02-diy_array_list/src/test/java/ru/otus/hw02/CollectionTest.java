package ru.otus.hw02;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CollectionTest {
    @Test
    public void addAll() {
        List<Number> testList = numbersTestList();

        Collections.addAll(testList, intNumbers());

        Assert.assertEquals(100, testList.size());
        Assert.assertTrue(testList.containsAll(numbersTestList()));
        Assert.assertTrue(testList.containsAll(Arrays.asList(intNumbers())));
    }

    @Test
    public void copy() {
        List<Number> testList = numbersTestList();
        List<Object> tmpList = Arrays.asList(intNumbers());

        List<Object> destinationList = Arrays.asList(intNumbers());

        Collections.copy(destinationList, testList);

        Assert.assertEquals(50, destinationList.size());
        Assert.assertTrue(destinationList.containsAll(testList));
        tmpList.forEach(num -> Assert.assertFalse(destinationList.contains(num)));
    }

    @Test
    public void sortAsc() {
        List<Integer> testList = testIntegersList();
        List<Integer> tmpList = testIntegersList();
        Collections.shuffle(testList);

        assertSortPreconditions(testList, tmpList);

        Collections.sort(testList, ascComparator());

        Assert.assertTrue(listElementsAreTheSame(testList, tmpList));
    }

    @Test
    public void sortDesc() {
        List<Integer> testList = testIntegersList();
        List<Integer> tmpList = testIntegersList();
        Collections.shuffle(testList);

        assertSortPreconditions(testList, tmpList);

        Collections.sort(testList, descComparator());
        Collections.reverse(tmpList);

        Assert.assertTrue(listElementsAreTheSame(testList, tmpList));
    }

    private void assertSortPreconditions(List testList, List tmpList) {
        Assert.assertFalse(testList.isEmpty());
        Assert.assertFalse(tmpList.isEmpty());
        Assert.assertFalse(listElementsAreTheSame(testList, tmpList));
    }

    private boolean listElementsAreTheSame(List first, List second) {
        int size = first.size();

        if (size != second.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (!first.get(i).equals(second.get(i))) {
                return false;
            }
        }

        return true;
    }

    private Comparator<? super Integer> ascComparator() {
        return new Comparator<Number>() {
            @Override
            public int compare(Number o1, Number o2) {
                return Integer.compare(o1.intValue(), o2.intValue());
            }
        };
    }

    private Comparator<? super Integer> descComparator() {
        return new Comparator<Number>() {
            @Override
            public int compare(Number o1, Number o2) {
                return Integer.compare(o2.intValue(), o1.intValue());
            }
        };
    }

    private List<Number> numbersTestList() {
        DIYArrayList<Number> result = new DIYArrayList<>();

        for (int i = 0; i < 50; i++) {
            result.add(i);
        }

        return result;
    }

    private List<Integer> testIntegersList() {
        DIYArrayList<Integer> result = new DIYArrayList<>();

        for (int i = 0; i < 50; i++) {
            result.add(i);
        }

        return result;
    }

    private Number[] intNumbers() {
        Number[] result = new Number[50];

        for (int i = 50; i < 100; i++) {
            result[i-50] = i;
        }

        return result;
    }
}
