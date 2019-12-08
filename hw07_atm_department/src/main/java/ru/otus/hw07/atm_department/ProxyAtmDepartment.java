package ru.otus.hw07.atm_department;

import ru.otus.hw07.atm_department.atm.WithdrawAndInsertMoney;
import java.util.List;

public class ProxyAtmDepartment implements IAtmDepartment {
    private IAtmDepartment department;

    public ProxyAtmDepartment(IAtmDepartment department) {
        this.department = department;

        System.out.println("New atm department opened. Atm list is:\r\n" + department.atmList());
    }

    @Override
    public List<WithdrawAndInsertMoney> atmList() {
        return department.atmList();
    }

    @Override
    public ProxyAtmDepartment restore() {
        System.out.println("Begin restoring all atm items of department to initial states...");

        var startTime = System.currentTimeMillis();

        // emulating process duration
        try {
            Thread.sleep(5000);
            System.out.println("Restoring in progress...");
        } catch (InterruptedException e) {

        }

        department.restore();

        System.out.println(
            "Restoring finished in " + (System.currentTimeMillis() - startTime) + " ms. "
        );

        return this;
    }

    @Override
    public int remains() {
        return department.remains();
    }
}
