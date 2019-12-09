package ru.otus.hw08.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Example {
    private  int number;
    private String message;
    transient private  boolean serialize = false;
    private int[] intArr = new int[]{5, 6, 7, 8};
    protected int abstractInt;
    private int[] emptyArr = new int[3];

    private OnlyTransients testObject;
    private String nullString = null;

    public Example(int number, String message) {
        this.number = number;
        this.message = message;
    }
}
