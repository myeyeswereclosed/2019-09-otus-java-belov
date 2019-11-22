package ru.otus.hw06.atm.atm_cell.withdrawal_result;

public class WithdrawalAvailable implements WithdrawalAvailabilityResult {
    private int nominal;
    private int banknotesNumber;

    public WithdrawalAvailable(int nominal, int banknotesNumber) {
        this.nominal = nominal;
        this.banknotesNumber = banknotesNumber;
    }

    @Override
    public int banknotesNumber() {
        return banknotesNumber;
    }

    @Override
    public int remains() {
        return 0;
    }

    @Override
    public int nominal() {
        return nominal;
    }

    @Override
    public String toString() {
        return "FullWithdrawal: banknotesNumber = " + banknotesNumber + "; nominal = " + nominal;
    }
}
