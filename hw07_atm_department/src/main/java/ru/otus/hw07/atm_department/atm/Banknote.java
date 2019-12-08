package ru.otus.hw07.atm_department.atm;

public class Banknote {
    private final int nominal;

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
