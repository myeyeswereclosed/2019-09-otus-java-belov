package ru.otus.hw06.atm.atm_cell.withdrawal_result;

public interface WithdrawalAvailabilityResult {
    // banknotes number available for withdrawal
    int banknotesNumber();

    // sum remained to withdraw if withdrawal occurs
    int remains();

    int nominal();
}
