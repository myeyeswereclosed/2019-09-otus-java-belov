package ru.otus.hw07.atm_department;

import ru.otus.hw07.atm_department.atm.WithdrawAndInsertMoney;
import ru.otus.hw07.atm_department.atm.command.InitCommand;
import java.util.*;

public class AtmDepartment implements IAtmDepartment {
    private Map<Integer, WithdrawAndInsertMoney> items = new HashMap<>();
    private Map<Integer, Map<InitCommand, WithdrawAndInsertMoney>> initialCommands = new HashMap<>();

    public AtmDepartment(List<InitCommand> commands) {
        int counter = 0;

        for (InitCommand command : commands) {
            WithdrawAndInsertMoney atm = command.execute();
            Map<InitCommand, WithdrawAndInsertMoney> initMap = new HashMap<>();

            items.put(counter, atm);
            initMap.put(command, atm);
            initialCommands.put(counter, initMap);
            counter++;
        }
    }

    @Override
    public List<WithdrawAndInsertMoney> atmList() {
        return new ArrayList<>(items.values());
    }

    @Override
    public AtmDepartment restore() {
        initialCommands.forEach(
            (atmId, commandToAtm) ->
                items.put(
                    atmId,
                    ((InitCommand) commandToAtm.keySet().toArray()[0]).restore()
            )
        );

        return this;
    }

    @Override
    public int remains() {
        return
            items
                .values()
                .stream()
                .mapToInt(MoneyStorage::remains)
                .sum()
        ;
    }
}
