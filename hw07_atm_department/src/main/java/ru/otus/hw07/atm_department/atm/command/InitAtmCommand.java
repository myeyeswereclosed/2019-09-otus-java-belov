package ru.otus.hw07.atm_department.atm.command;

import ru.otus.hw07.atm_department.atm.IAtm;

public interface InitAtmCommand {
    IAtm execute();

    IAtm restore();
}
