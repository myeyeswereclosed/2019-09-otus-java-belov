package ru.otus.hw07.atm_department.atm.command;

import ru.otus.hw07.atm_department.atm.Atm;
import ru.otus.hw07.atm_department.atm.IAtm;
import ru.otus.hw07.atm_department.atm.atm_cell.AtmCell;
import ru.otus.hw07.atm_department.atm.atm_cell.BanknoteCell;
import ru.otus.hw07.atm_department.money_distributing.DistributingStrategy;
import java.util.*;
import java.util.stream.Collectors;

public class InitCommand implements InitAtmCommand {
    private Map<Integer, Integer> initialState = new HashMap<>();
    private final List<Integer> banknotes;
    private final int banknotesMaxNumber;
    private DistributingStrategy distributingStrategy;

    public InitCommand(
        List<Integer> banknotes,
        int banknotesMaxNumber,
        DistributingStrategy banknotesStrategy
    ) {
        this.banknotes = banknotes;
        this.banknotesMaxNumber = banknotesMaxNumber;
        distributingStrategy = banknotesStrategy;
    }

    public IAtm execute() {
        List<AtmCell> cells = new ArrayList<>();

        initialState = distributingStrategy.banknotesNumberMap(banknotes, banknotesMaxNumber);

        initialState.forEach(
            (nominal, banknotesNumber) -> cells.add(new BanknoteCell(nominal, banknotesNumber))
        );

        return new Atm(cells);
    }

    public IAtm restore() {
        return
            new Atm(
                initialState
                    .entrySet()
                    .stream()
                    .map(entry -> new BanknoteCell(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList())
            );
    }
}
