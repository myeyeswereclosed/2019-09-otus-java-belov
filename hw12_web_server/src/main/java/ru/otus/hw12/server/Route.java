package ru.otus.hw12.server;

import java.util.Arrays;
import java.util.List;

public enum Route {
        LOGIN("/login"),
        USERS("/users"),
        USER("/api/user/*"),
        ADMIN("/admin"),
        ADD_USER("/admin/users/add"),
        ALL_USERS("/admin/users")
    ;

    public final static List<Route> secured = Arrays.asList(USERS, USER, ADMIN, ADD_USER, ALL_USERS);

    private String path;

    private Route(String path) {
        this.path = path;
    }

    public String path() {
        return path;
    }
}
