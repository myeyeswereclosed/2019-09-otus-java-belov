package ru.otus.hw07.atm_department.atm.atm_cell;

import ru.otus.hw07.atm_department.atm.Banknote;
import java.util.Map;

public interface AtmCell {
    int nominal();

    int banknotesNumber();

    Map<Integer, Integer> withdrawResult(int sum);

    AtmCell withdraw(int banknotesNumber);

    AtmCell add(Banknote banknote);

    default boolean ofNominal(int nominalSupposed) {
        return nominal() == nominalSupposed;
    }
}
