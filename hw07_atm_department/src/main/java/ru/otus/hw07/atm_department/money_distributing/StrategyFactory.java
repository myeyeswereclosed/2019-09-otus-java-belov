package ru.otus.hw07.atm_department.money_distributing;

public class StrategyFactory {
    public static DistributingStrategy create(DistributingStrategyEnum strategyName) {
        if (strategyName.isRandom()) {
            return new RandomStrategy();
        }

        return new SimpleStrategy();
    }
}
