import org.junit.Assert;
import org.junit.Test;
import ru.otus.hw06.atm.Atm;
import ru.otus.hw06.atm.Banknote;
import ru.otus.hw06.atm.atm_cell.AtmCell;
import ru.otus.hw06.atm.atm_cell.BanknoteCell;
import java.util.Arrays;

public class AtmTest {
    private  AtmCell hundredsCell = new BanknoteCell(100, 4);
    private  AtmCell fiftiesCell = new BanknoteCell(50, 8);
    private  AtmCell tensCell = new BanknoteCell(10, 12);
    private  AtmCell threesCell = new BanknoteCell(3, 19);

    @Test
    public void canWithdraw() {
        Atm atm = new Atm(Arrays.asList(threesCell, fiftiesCell, tensCell, hundredsCell));

        Assert.assertEquals("Sum remained in ATM = 977", atm.remains());

        atm.withdraw(279);

        Assert.assertEquals("Sum remained in ATM = 698", atm.remains());
    }

    @Test
    public void withdrawalUnavailable() {
        Atm atm = new Atm(Arrays.asList(threesCell, fiftiesCell, tensCell, hundredsCell));

        Assert.assertEquals("Sum remained in ATM = 977", atm.remains());

        atm.withdraw(278);

        Assert.assertEquals("Sum remained in ATM = 977", atm.remains());
    }

    @Test
    public void insertOk() {
        Atm atm = new Atm(Arrays.asList(threesCell, fiftiesCell, tensCell, hundredsCell));

        Assert.assertEquals("Sum remained in ATM = 977", atm.remains());

        atm.insert(new Banknote(3));

        Assert.assertEquals("Sum remained in ATM = 980", atm.remains());
    }

    @Test
    public void insertFail() {
        Atm atm = new Atm(Arrays.asList(threesCell, fiftiesCell, tensCell, hundredsCell));

        Assert.assertEquals("Sum remained in ATM = 977", atm.remains());

        atm.withdraw(278);

        Assert.assertEquals("Sum remained in ATM = 977", atm.remains());
    }

    @Test
    public void withdrawAll() {
        Atm atm = new Atm(Arrays.asList(threesCell, fiftiesCell, tensCell, hundredsCell));

        Assert.assertEquals("Sum remained in ATM = 977", atm.remains());

        atm.withdraw(977);

        Assert.assertEquals("Sum remained in ATM = 0", atm.remains());
        Assert.assertEquals("Sorry, withdrawal is unavailable for your sum. ", atm.withdraw(10));
        Assert.assertEquals("Sum remained in ATM = 0", atm.remains());
    }

    @Test
    public void withdrawTooMuch() {
        Atm atm = new Atm(Arrays.asList(threesCell, fiftiesCell, tensCell, hundredsCell));

        Assert.assertEquals("Sum remained in ATM = 977", atm.remains());
        Assert.assertEquals("Sorry, withdrawal is unavailable for your sum. ", atm.withdraw(1000));
        Assert.assertEquals("Sum remained in ATM = 977", atm.remains());
    }
}
