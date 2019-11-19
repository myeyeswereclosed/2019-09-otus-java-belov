package ru.otus.hw06.atm.atm_cell;

import ru.otus.hw06.atm.Banknote;
import ru.otus.hw06.atm.atm_cell.withdrawal_result.WithdrawalAvailabilityResult;
import ru.otus.hw06.atm.atm_cell.withdrawal_result.WithdrawalUnavailable;
import ru.otus.hw06.atm.atm_cell.withdrawal_result.WithdrawalAvailable;
import ru.otus.hw06.atm.atm_cell.withdrawal_result.PartiallyAvailableWithdrawal;

public class BanknoteCell implements AtmCell {
    private int nominal;
    private int banknotesNumber;

    public BanknoteCell(int nominal, int banknotesNumber) {
        this.nominal = nominal;
        this.banknotesNumber = banknotesNumber;
    }

    @Override
    public int nominal() {
        return nominal;
    }

    @Override
    public int banknotesNumber() {
        return banknotesNumber;
    }

    @Override
    public AtmCell add(Banknote banknote) {
        if (banknote.value() == nominal) {
            banknotesNumber++;
        }

        return this;
    }

    @Override
    public WithdrawalAvailabilityResult withdrawResult(int sum) {
        if (sum < nominal || banknotesNumber == 0) {
            return new WithdrawalUnavailable(sum, nominal);
        }

        if (sum % nominal == 0) {
            return withdrawValid(sum);
        }

        int banknotesNumber = getBanknotesNumber(sum - (sum % nominal));

        return new PartiallyAvailableWithdrawal(nominal, banknotesNumber, sum - nominal*banknotesNumber);
    }

    @Override
    public AtmCell withdraw(int banknotesNumber) {
        this.banknotesNumber -= banknotesNumber;

        return this;
    }

    @Override
    public String toString() {
        return "Cell: nominal = " + nominal + "; banknotes totally: " + banknotesNumber;
    }

    private WithdrawalAvailabilityResult withdrawValid(int sum) {
        return
            banknotesNumber * nominal >= sum
                ? new WithdrawalAvailable(nominal, sum / nominal)
                : new PartiallyAvailableWithdrawal(nominal, banknotesNumber, sum - nominal*banknotesNumber)
        ;
    }

    private int getBanknotesNumber(int sum) {
        return Math.min(banknotesNumber, sum / nominal);
    }
}
