package ru.otus.hw06.atm.atm_cell;

import ru.otus.hw06.atm.Banknote;
import ru.otus.hw06.atm.atm_cell.withdrawal_result.WithdrawalAvailabilityResult;

public interface AtmCell {
    int nominal();

    int banknotesNumber();

    WithdrawalAvailabilityResult withdrawResult(int sum);

    AtmCell withdraw(int banknotesNumber);

    AtmCell add(Banknote banknote);

    default boolean isEmpty() {
        return banknotesNumber() == 0;
    }

    default boolean ofNominal(int nominalSupposed) {
        return nominal() == nominalSupposed;
    }
}
