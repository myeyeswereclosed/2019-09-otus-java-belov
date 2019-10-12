package ru.otus.hw02.cat;

public class HomeCat extends Cat {
    private String name;

    public HomeCat(int weight, String name) {
        super(weight);

        this.name = name;
    }

    public String toString() {
        return "Home cat: {weight=" + weight() + "; name=" + name + '}';
    }
}
