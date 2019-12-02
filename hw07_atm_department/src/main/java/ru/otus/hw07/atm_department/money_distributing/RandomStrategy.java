package ru.otus.hw07.atm_department.money_distributing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomStrategy implements DistributingStrategy {
    @Override
    public Map<Integer, Integer> banknotesNumberMap(List<Integer> banknotes, int maxBanknotesNumber) {
        Map<Integer, Integer> result = new HashMap<>();

        banknotes.forEach(banknote -> result.put(banknote, new Random().nextInt(maxBanknotesNumber)));

        return result;
    }
}
