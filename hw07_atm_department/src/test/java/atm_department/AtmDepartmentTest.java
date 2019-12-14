package atm_department;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.hw07.atm_department.AtmDepartment;
import ru.otus.hw07.atm_department.atm.command.InitCommand;
import ru.otus.hw07.atm_department.money_distributing.DistributingStrategyEnum;
import ru.otus.hw07.atm_department.money_distributing.StrategyFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AtmDepartmentTest {
    private static List<Integer> banknotes1 = Arrays.asList(10, 50, 100, 500, 1000);
    private static List<Integer> banknotes2 = Arrays.asList(10, 50, 100, 200, 500, 1000, 2000, 5000);

    @Test
    public void department() {
        AtmDepartment department =
            new AtmDepartment(
                new ArrayList<>() {{
                    add(new InitCommand(banknotes1, 15, StrategyFactory.create(DistributingStrategyEnum.RANDOM)));
                    add(new InitCommand(banknotes2, 20, StrategyFactory.create(DistributingStrategyEnum.SIMPLE)));
                }}
            );

        Assert.assertEquals(2, department.atmList().size());

        var initialSum = department.remains();

        department.atmList().forEach(atm -> atm.withdraw(100));

        Assert.assertEquals(200, initialSum - department.remains());
        department.restore();
        Assert.assertEquals(initialSum, department.remains());
    }
}
