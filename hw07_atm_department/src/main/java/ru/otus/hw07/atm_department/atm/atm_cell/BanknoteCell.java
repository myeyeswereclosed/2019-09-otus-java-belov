package ru.otus.hw07.atm_department.atm.atm_cell;

import ru.otus.hw07.atm_department.atm.Banknote;
import java.util.HashMap;
import java.util.Map;

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
    public Map<Integer, Integer> withdrawResult(int sum) {
        Map<Integer, Integer> result = new HashMap<>();

        if (sum < nominal || banknotesNumber == 0) {
            result.put(nominal, 0);

            return result;
        }

        if (sum % nominal == 0) {
            return withdrawValid(sum);
        }

        result.put(nominal, getBanknotesNumber(sum - (sum % nominal)));

        return result;
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

    private Map<Integer, Integer> withdrawValid(int sum) {
        Map<Integer, Integer> result = new HashMap<>();

        if (banknotesNumber * nominal >= sum) {
            result.put(nominal, sum / nominal);
        } else {
            result.put(nominal, banknotesNumber);
        }

        return result;
    }

    private int getBanknotesNumber(int sum) {
        return Math.min(banknotesNumber, sum / nominal);
    }
}
