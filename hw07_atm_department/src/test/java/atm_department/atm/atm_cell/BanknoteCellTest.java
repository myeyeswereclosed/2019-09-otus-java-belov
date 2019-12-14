package atm_department.atm.atm_cell;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.hw07.atm_department.atm.atm_cell.AtmCell;
import ru.otus.hw07.atm_department.atm.atm_cell.BanknoteCell;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BanknoteCellTest {
    private static AtmCell hundredCell = new BanknoteCell(100, 4);

    @Test
    public void fail() {
        int sum = 99;

        Map<Integer, Integer> result = hundredCell.withdrawResult(sum);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(100, result.keySet().toArray()[0]);
        Assert.assertEquals(0, (int)result.get(100));
    }

    @Test
    public void partial() {
        List<Integer> sumsForPartial = Arrays.asList(455, 255);

        sumsForPartial.forEach(
            sum -> {
                Map<Integer, Integer> result = hundredCell.withdrawResult(sum);

                Assert.assertEquals(1, result.size());
                Assert.assertEquals(100, result.keySet().toArray()[0]);
                Assert.assertEquals(55, sum - result.get(100) * 100);
            }
        );
    }

    @Test
    public void full() {
        Map<Integer, Integer> result = hundredCell.withdrawResult(300);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(100, result.keySet().toArray()[0]);
        Assert.assertEquals(3, (int)result.get(100));
    }

    @Test
    public void nominalAndNumber() {
        Assert.assertEquals(100, hundredCell.nominal());
        Assert.assertEquals(100, hundredCell.nominal());
    }
}
