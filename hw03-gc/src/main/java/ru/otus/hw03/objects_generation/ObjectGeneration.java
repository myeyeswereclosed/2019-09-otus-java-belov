package ru.otus.hw03.objects_generation;

public enum ObjectGeneration {
    YOUNG("Young Gen"),
    OLD("Old Gen");

    private String name;

    ObjectGeneration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
