package ru.otus.hw07.atm_department.money_distributing;

import java.util.List;
import java.util.Map;

public interface DistributingStrategy {
    // some algorithm to distribute money
    Map<Integer, Integer> banknotesNumberMap(List<Integer> banknotes, int maxBanknotesNumber);
}
