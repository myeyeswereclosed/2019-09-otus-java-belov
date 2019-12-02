package ru.otus.hw07;

import ru.otus.hw07.atm_department.AtmDepartment;
import ru.otus.hw07.atm_department.IAtmDepartment;
import ru.otus.hw07.atm_department.ProxyAtmDepartment;
import ru.otus.hw07.atm_department.atm.command.InitCommand;
import ru.otus.hw07.atm_department.money_distributing.StrategyFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AtmDepartmentMain {
    private static List<Integer> banknotes1 = Arrays.asList(10, 50, 100, 500, 1000);
    private static List<Integer> banknotes2 = Arrays.asList(10, 50, 100, 200, 500, 1000, 2000, 5000);

    public static void main(String[] args) {
        IAtmDepartment department =
            new ProxyAtmDepartment(
                new AtmDepartment(
                    new ArrayList<>() {{
                        add(new InitCommand(banknotes1, 15, StrategyFactory.random()));
                        add(new InitCommand(banknotes2, 20, StrategyFactory.simple()));
                    }}
                )
            );

        System.out.println("All money in department " + department.remains());

        department.atmList().forEach(atm -> atm.withdraw(100));

        System.out.println("All money after withdrawals " + department.remains());

        System.out.println(department.restore().remains());
    }
}
