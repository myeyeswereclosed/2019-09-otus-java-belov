package ru.otus.hw07.atm_department;

import ru.otus.hw07.atm_department.atm.WithdrawAndInsertMoney;
import java.util.List;

public interface IAtmDepartment extends MoneyStorage {
    List<WithdrawAndInsertMoney> atmList();

    // restore to initial state
    IAtmDepartment restore();
}
