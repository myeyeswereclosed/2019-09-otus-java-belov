package ru.otus.hw02.cat;

public class Cat extends Animal implements Comparable<Cat> {
    private int weight;

    public Cat(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Cat: {weight=" + weight + '}';
    }

    int weight() {
        return weight;
    }

    @Override
    public int compareTo(Cat cat) {
        return Integer.compare(weight, cat.weight());
    }
}
