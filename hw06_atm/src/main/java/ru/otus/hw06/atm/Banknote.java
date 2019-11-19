package ru.otus.hw06.atm;

public class Banknote {
    private int nominal;

    public Banknote(int nominal) {
        this.nominal = nominal;
    }

    public int value() {
        return nominal;
    }

    @Override
    public String toString() {
        return "Banknote of nominal " + nominal;
    }
}
