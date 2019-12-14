package ru.otus.hw07.atm_department.money_distributing;

public enum DistributingStrategyEnum {
    SIMPLE, RANDOM;

    public boolean isSimple() {
        return this == SIMPLE;
    }

    public boolean isRandom() {
        return this == RANDOM;
    }
}
