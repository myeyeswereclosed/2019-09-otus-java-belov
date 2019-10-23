package ru.otus.hw03.leaking_list;

import java.util.ArrayList;
import java.util.List;

public class LeakingList implements LeakingListMBean {
    private static List<String> contents = new ArrayList<>();

    public void addElements() {
        while (true) {
            // young generation garbage
            String[] strings = new String[500_000];

            for (int k = 0; k < 500_000 ; k++) {
                strings[k] = String.valueOf(k);
            }

            //
            for (int j = 0; j < 4000; j++) {
                contents.add(String.valueOf(j));
            }

            // remove some elements
            contents.removeIf(string -> (Integer.parseInt(string) % 2 == 0));
        }
    }

    public int size() {
        return contents.size();
    }
}
