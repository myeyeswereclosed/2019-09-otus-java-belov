package ru.otus.hw06.atm.atm_cell.withdrawal_result;

public class WithdrawalUnavailable implements WithdrawalAvailabilityResult {
    private int sum;
    private int nominal;

    public WithdrawalUnavailable(int sum, int nominal) {
        this.sum = sum;
        this.nominal = nominal;
    }

    @Override
    public int banknotesNumber() {
        return 0;
    }

    @Override
    public int remains() {
        return sum;
    }

    @Override
    public int nominal() {
        return nominal;
    }

    @Override
    public String toString() {
        return "FailWithdrawal: sum = " + sum + "; nominal = " + nominal;
    }
}
