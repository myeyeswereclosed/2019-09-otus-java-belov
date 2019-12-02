package ru.otus.hw07.atm_department.atm;

import ru.otus.hw07.atm_department.atm.atm_cell.AtmCell;
import java.util.*;
import java.util.stream.Collectors;

public class Atm implements IAtm {
    private static final String WITHDRAWAL_OK_MESSAGE = "Please, take your money. Sum remained in ATM = ";
    private static final String WITHDRAWAL_UNAVAILABLE_MESSAGE = "Sorry, withdrawal is unavailable for your sum. ";
    private static final String BANKNOTE_REJECTED_MESSAGE = "Banknote rejected";

    private TreeSet<AtmCell> cells;

    public Atm(List<AtmCell> cells) {
        this.cells = nonEmptyOrderedCells(cells);
    }

    public String withdraw(int sum) {
        int total = remains();

        if (total == 0 || sum > total) {
            return WITHDRAWAL_UNAVAILABLE_MESSAGE;
        }

        Map<Integer, Map<Integer, Integer>> cellsResults = withdrawalAvailabilityResult(sum);

        int remains = (int)cellsResults.keySet().toArray()[0];

        return
            remains == 0
                ? withdraw(cellsResults.get(remains))
                : WITHDRAWAL_UNAVAILABLE_MESSAGE
        ;
    }

    private String withdraw(Map<Integer, Integer> results) {
        results
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() > 0)
            .forEach(
                entry ->
                    cells
                        .stream()
                        .filter(cell -> cell.ofNominal(entry.getKey()))
                        .findFirst()
                        .map(cell -> cell.withdraw(entry.getValue()))
            );

        return WITHDRAWAL_OK_MESSAGE + remains();
    }

    @Override
    public String insert(Banknote banknote) {
        return
            cells
                .stream()
                .filter(cell -> cell.ofNominal(banknote.value()))
                .findFirst()
                .map(cell -> {
                    cell.add(banknote);

                    return "Banknote = " + banknote.value() + " accepted";
                })
                .orElse(BANKNOTE_REJECTED_MESSAGE)
        ;
    }

    @Override
    public int remains() {
        return
            cells
                .stream()
                .mapToInt(cell -> cell.nominal() * cell.banknotesNumber())
                .sum()
            ;
    }

    @Override
    public String toString() {
        return "Atm: {" + cells.stream().map(Object::toString).collect(Collectors.joining("\r\n")) + '}';
    }

    public Map<Integer, Map<Integer, Integer>> withdrawalAvailabilityResult(int sum) {
        Map<Integer, Integer> cellsResult = new HashMap<>();
        int currentSum = sum;

        for (AtmCell cell : cells) {
            Map<Integer,Integer> cellResult = cell.withdrawResult(currentSum);
            cellsResult.putAll(cellResult);
            currentSum -= cell.nominal() * cellResult.get(cell.nominal());
        }

        Map<Integer, Map<Integer, Integer>> result = new HashMap<>();

        result.put(currentSum, cellsResult);

        return result;
    }

    // sort cells by nominal desc to withdraw with greater banknotes first
    private TreeSet<AtmCell> nonEmptyOrderedCells(List<AtmCell> cells) {
        TreeSet result =
            new TreeSet(
                (Comparator<AtmCell>) (o1, o2) -> {
                    if (o1.nominal() == o2.nominal()) {
                        return 0;
                    }

                    return o1.nominal() >= o2.nominal() ? -1 : 1;
                }
            );

        result.addAll(cells);

        return result;
    }
}
