package ru.otus.hw02;

import ru.otus.hw02.cat.Cat;
import ru.otus.hw02.cat.HomeCat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    private static int catsNumber = 30;

    public static void main(String[] args) {
        addAll();
        copy();
        sort();
    }

    // cats and home cats
    private static void addAll() {
        List<Cat> cats = makeCats();
        HomeCat[] homeCats = makeHomeCats();

        System.out.println("*********** Collections - Add all ***********");

        Collections.addAll(cats, homeCats);

        cats.forEach(System.out::println);
    }

    // only home cats as result
    private static void copy() {
        List<Cat> cats = makeCats();
        HomeCat[] homeCats = makeHomeCats();

        System.out.println("*********** Collections - Copy ***********");

        Collections.copy(cats, Arrays.asList(homeCats));

        cats.forEach(System.out::println);
    }

    // sort cats and home cats by weight
    private static void sort() {
        List<Cat> cats = makeCats();
        HomeCat[] homeCats = makeHomeCats();

        System.out.println("*********** Collections - Sort ***********");

        Collections.addAll(cats, homeCats);
        Collections.sort(cats);

        cats.forEach(System.out::println);
    }

    private static DIYArrayList<Cat> makeCats() {
        DIYArrayList<Cat> cats = new DIYArrayList<>();

        for (int i = 0; i < catsNumber; i++) {
            cats.add(new Cat(randomWeight()));
        }

        return cats;
    }

    private static HomeCat[] makeHomeCats() {
        HomeCat[] cats = new HomeCat[catsNumber];

        for (int i = 0; i < catsNumber; i++) {
            cats[i] = new HomeCat(randomWeight(), "Kitten number " + i);
        }

        return cats;
    }

    private static int randomWeight() {
        return (int)(Math.random()*10) + 10;
    }
}
