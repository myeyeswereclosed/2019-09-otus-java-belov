package ru.otus.hw06;

import ru.otus.hw06.atm.Atm;
import ru.otus.hw06.atm.Banknote;
import ru.otus.hw06.atm.IAtm;
import ru.otus.hw06.atm.atm_cell.AtmCell;
import ru.otus.hw06.atm.atm_cell.BanknoteCell;
import java.util.Arrays;
import java.util.List;

public class AtmMain {
    public static void main(String[] args) throws Exception {
        IAtm atm = new Atm(cells());

        System.out.println(atm.withdraw(273));
        System.out.println(atm.remains());
        System.out.println(atm.withdraw(132));
        System.out.println(atm.insert(new Banknote(200)));
        System.out.println(atm.insert(new Banknote(100)));
        System.out.println(atm.withdraw(403));
        System.out.println(atm.remains());
    }

    private static List<AtmCell> cells() {
        return
            Arrays.asList(
                new BanknoteCell(100, 5),
                new BanknoteCell(50, 8),
                new BanknoteCell(10, 10),
                new BanknoteCell(3, 18)
            );
    }
}
