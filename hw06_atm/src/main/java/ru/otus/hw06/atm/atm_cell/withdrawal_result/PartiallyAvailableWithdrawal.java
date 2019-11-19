package ru.otus.hw06.atm.atm_cell.withdrawal_result;

public class PartiallyAvailableWithdrawal implements WithdrawalAvailabilityResult {
    private int banknotesNumber;
    private int nominal;
    private int remains;

    public PartiallyAvailableWithdrawal(int nominal, int banknotesNumber, int remains) {
        this.banknotesNumber = banknotesNumber;
        this.nominal = nominal;
        this.remains = remains;
    }

    @Override
    public int banknotesNumber() {
        return banknotesNumber;
    }

    @Override
    public int remains() {
        return remains;
    }

    @Override
    public int nominal() {
        return nominal;
    }

    @Override
    public String toString() {
        return
            "PartialWithdrawal: banknotesNumber = " + banknotesNumber +
            "; nominal = " + nominal +
            "; remains = " + remains;
    }
}
