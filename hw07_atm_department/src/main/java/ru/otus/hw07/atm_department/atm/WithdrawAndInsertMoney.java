package ru.otus.hw07.atm_department.atm;

import ru.otus.hw07.atm_department.MoneyStorage;

public interface WithdrawAndInsertMoney extends MoneyStorage {
    String withdraw(int sum);

    String insert(Banknote banknote);
}
