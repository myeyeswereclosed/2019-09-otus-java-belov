package ru.otus.hw02;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.ListIterator;

public class DIYListIteratorTest {
    @Test
    public void set() {
        List<Integer> diyList = createIntegersDiyList();
        ListIterator<Integer> listIterator = diyList.listIterator();

        listIterator.next();
        listIterator.set(15);

        Assert.assertEquals(15, (int)diyList.get(0));
    }

    @Test
    public void previousIndex() {
        List<Integer> diyList = createIntegersDiyList();
        ListIterator<Integer> listIterator = diyList.listIterator();

        Assert.assertEquals(-1, listIterator.previousIndex());
    }

    private List<Integer> createIntegersDiyList() {
        List<Integer> diyList = new DIYArrayList<>();

        diyList.add(1);
        diyList.add(null);
        diyList.add(5);

        return diyList;
    }
}
