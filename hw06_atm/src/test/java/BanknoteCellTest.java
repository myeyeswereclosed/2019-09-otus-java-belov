import org.junit.Assert;
import org.junit.Test;
import ru.otus.hw06.atm.atm_cell.AtmCell;
import ru.otus.hw06.atm.atm_cell.BanknoteCell;
import ru.otus.hw06.atm.atm_cell.withdrawal_result.WithdrawalAvailabilityResult;
import ru.otus.hw06.atm.atm_cell.withdrawal_result.WithdrawalUnavailable;
import ru.otus.hw06.atm.atm_cell.withdrawal_result.WithdrawalAvailable;
import ru.otus.hw06.atm.atm_cell.withdrawal_result.PartiallyAvailableWithdrawal;
import java.util.Arrays;
import java.util.List;

public class BanknoteCellTest {
    private static AtmCell hundredCell = new BanknoteCell(100, 4);

    @Test
    public void fail() {
        int sum = 99;

        Assert.assertTrue(hundredCell.withdrawResult(sum) instanceof WithdrawalUnavailable);
    }

    @Test
    public void partial() {
        List<Integer> sumsForPartial = Arrays.asList(455, 255);

        sumsForPartial.forEach(
            sum -> {
                WithdrawalAvailabilityResult result = hundredCell.withdrawResult(sum);

                Assert.assertTrue(result instanceof PartiallyAvailableWithdrawal);
                Assert.assertEquals(55, result.remains());
            }
        );
    }

    @Test
    public void full() {
        WithdrawalAvailabilityResult result = hundredCell.withdrawResult(300);

        Assert.assertTrue(result instanceof WithdrawalAvailable);
        Assert.assertEquals(3, result.banknotesNumber());
    }

    @Test
    public void nominalAndNumber() {
        Assert.assertEquals(100, hundredCell.nominal());
        Assert.assertEquals(100, hundredCell.nominal());
    }
}
