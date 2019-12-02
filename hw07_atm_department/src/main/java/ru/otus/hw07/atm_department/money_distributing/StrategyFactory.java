package ru.otus.hw07.atm_department.money_distributing;

public class StrategyFactory {
    public static DistributingStrategy simple() {
        return new SimpleStrategy();
    }

    public static DistributingStrategy random() {
        return new RandomStrategy();
    }
}
