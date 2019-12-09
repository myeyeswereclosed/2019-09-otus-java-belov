package ru.otus.hw08;

import ru.otus.hw08.example.Example;
import ru.otus.hw08.example.OnlyTransients;
import ru.otus.hw08.json_writer.MyJsonWriter;

public class JsonWriterMain {
    public static void main(String[] args) throws Exception {
        MyJsonWriter gson = new MyJsonWriter();

        System.out.println(gson.asJsonString(null));
        System.out.println(gson.asJsonString(1));
        System.out.println(gson.asJsonString(1.2));
        System.out.println(gson.asJsonString(new Example(6, "six")));
        System.out.println(gson.asJsonString(new OnlyTransients()));
    }
}
