package ru.otus.hw06.atm;

import ru.otus.hw06.atm.atm_cell.AtmCell;
import ru.otus.hw06.atm.atm_cell.withdrawal_result.WithdrawalAvailabilityResult;
import java.util.*;
import java.util.stream.Collectors;

public class Atm implements IAtm {
    private static final String WITHDRAWAL_OK_MESSAGE = "Please, take your money. ";
    private static final String WITHDRAWAL_UNAVAILABLE_MESSAGE = "Sorry, withdrawal is unavailable for your sum. ";
    private static final String BANKNOTE_REJECTED_MESSAGE = "Banknote rejected";

    private List<AtmCell> cells;

    public Atm(List<AtmCell> cells) {
        this.cells = nonEmptyOrderedCells(cells);
    }

    public String withdraw(int sum) {
        int total = total();

        if (total == 0 || sum > total) {
            return WITHDRAWAL_UNAVAILABLE_MESSAGE;
        }

        List<WithdrawalAvailabilityResult> cellsResults = withdrawalAvailabilityResult(sum);

        return
            withdrawalIsAvailable(cellsResults)
                ? withdraw(cellsResults)
                : WITHDRAWAL_UNAVAILABLE_MESSAGE
        ;
    }

    private String withdraw(List<WithdrawalAvailabilityResult> results) {
        results
            .stream()
            .filter(result -> result.banknotesNumber() > 0)
            .forEach(
                result ->
                    cells
                        .stream()
                        .filter(cell -> cell.ofNominal(result.nominal()))
                        .findFirst()
                        .map(cell -> cell.withdraw(result.banknotesNumber()))
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
    public String remains() {
        return "Sum remained in ATM = " + total();
    }

    private int total() {
        return
            nonEmptyCells(cells)
                .stream()
                .mapToInt(cell -> cell.nominal() * cell.banknotesNumber())
                .sum()
        ;
    }

    public List<WithdrawalAvailabilityResult> withdrawalAvailabilityResult(int sum) {
        List<WithdrawalAvailabilityResult> result = new ArrayList<>();
        int currentSum = sum;

        for (AtmCell cell: nonEmptyCells(cells)) {
            WithdrawalAvailabilityResult withdrawalResult = cell.withdrawResult(currentSum);

            result.add(withdrawalResult);

            if (withdrawalResult.remains() == 0) {
                return result;
            }

            currentSum = withdrawalResult.remains();
        }

        return result;
    }

    // if there is no remainder after last cell -> withdrawal is available
    private boolean withdrawalIsAvailable(List<WithdrawalAvailabilityResult> cellResults) {
        return !cellResults.isEmpty() && cellResults.get(cellResults.size() - 1).remains() == 0;
    }

    // sort cells by nominal desc to withdraw with greater banknotes first
    private List<AtmCell> nonEmptyOrderedCells(List<AtmCell> cells) {
        return
            nonEmptyCells(cells)
                .stream()
                .sorted((o1, o2) -> {
                    if (o1.nominal() == o2.nominal()) {
                        return 0;
                    }

                    return o1.nominal() >= o2.nominal() ? -1 : 1;
                })
                .collect(Collectors.toList())
            ;
    }

    private List<AtmCell> nonEmptyCells(List<AtmCell> cells) {
        return
            cells
                .stream()
                .filter(cell -> !cell.isEmpty())
                .collect(Collectors.toList())
        ;
    }
}
