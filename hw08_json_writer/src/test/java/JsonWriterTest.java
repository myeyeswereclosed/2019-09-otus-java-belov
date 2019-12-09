import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import ru.otus.hw08.example.Example;
import ru.otus.hw08.example.Examples;
import ru.otus.hw08.example.OnlyTransients;
import ru.otus.hw08.json_writer.MyJsonWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonWriterTest {
    @Test
    public void nullObj() {
        Assert.assertEquals(
            new Gson().toJson(null),
            new MyJsonWriter().asJsonString(null)
        );
    }

    @Test
    public void primitiveObjects() {
        Arrays.stream(primitives()).forEach(
            primitive ->
                Assert.assertEquals(
                    new Gson().toJson(primitive),
                    new MyJsonWriter().asJsonString(primitive)
                )
        );
    }

    @Test
    public void stringObjects() {
        Arrays.stream(strings()).forEach(
            primitive ->
                Assert.assertEquals(
                    new Gson().toJson(primitive),
                    new MyJsonWriter().asJsonString(primitive)
                )
        );
    }

    @Test
    public void primitiveArraysTest() {
        Arrays.stream(primitivesArrays()).forEach(
            primitive ->
                Assert.assertEquals(
                    new Gson().toJson(primitive),
                    new MyJsonWriter().asJsonString(primitive)
                )
        );
    }

    @Test
    public void primitivesArray() {
        char[] chars = chars();

        Assert.assertEquals(
            new Gson().toJson(chars),
            new MyJsonWriter().asJsonString(chars)
        );
    }

    @Test
    public void stringListTest() {
        List<String> stringList = stringList();

        Assert.assertEquals(
            new Gson().toJson(stringList),
            new MyJsonWriter().asJsonString(stringList)
        );
    }

    @Test
    public void test() {
        var testObject = new TestObject();
        Assert.assertEquals(
            new Gson().toJson(testObject),
            new MyJsonWriter().asJsonString(testObject)
        );
    }

    @Test
    public void onlyTransients() {
        var testObject = new OnlyTransients();
        Assert.assertEquals(
            new Gson().toJson(testObject),
            new MyJsonWriter().asJsonString(testObject)
        );
    }

    private Object[] primitives() {
        return new Object[]{true, false, '?', 1, 1.2, 1.2e4, 100L, 500_000, 4/3};
    }

    private Object[] strings() {
        return new Object[]{"", "test", "123", "rn\r\n"};
    }

    private char[] chars() {
        return new char[]{'a', 'b', '?', '\t'};
    }

    private Object[][] primitivesArrays() {
        return
            new Object[][]{
                new Object[]{1, 2, 3},
                new Object[]{true, false},
                new Object[]{1, false},
                new Object[]{1, 1.2, 100L},
            };
    }

    private List<String> stringList() {
        List<String> stringList = new ArrayList<>();

        stringList.add("mir");
        stringList.add(null);
        stringList.add("123");
        stringList.add("");

        return stringList;
    }

    private static class TestObject {
        private String first = "1";
        private int second = 2;
        private boolean third = false;
        private long longField = 1L;
        private int[] intArr = new int[]{1, 2, 3, 4};
        private List<Integer> intList = Arrays.asList(1, 2, 4, 7);
        private List<String> strList = Arrays.asList("ololo", "trololo", "shkololo");
        private List<Example> inners =
            Arrays.asList(
                new Example(1, "first message"),
                new Example(2, "second message")
            );
        private Examples[] innerExamples =
           new Examples[] {new Examples(), null, new Examples()};
    }
}
