package ru.otus.hw07.atm_department;

import ru.otus.hw07.atm_department.atm.IAtm;
import java.util.List;

public interface IAtmDepartment extends MoneyStorage {
    List<IAtm> atmList();

    // restore to initial state
    IAtmDepartment restore();
}
