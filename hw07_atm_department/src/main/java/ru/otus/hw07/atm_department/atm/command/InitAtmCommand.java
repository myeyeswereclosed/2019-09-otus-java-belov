package ru.otus.hw07.atm_department.atm.command;

import ru.otus.hw07.atm_department.atm.WithdrawAndInsertMoney;

public interface InitAtmCommand {
    WithdrawAndInsertMoney execute();

    WithdrawAndInsertMoney restore();
}
