package ru.otus.hw12.servlet.admin;

import com.google.gson.Gson;
import ru.otus.hw12.services.common.UserService;
import ru.otus.hw12.servlet.BaseServlet;
import ru.otus.hw12.servlet.ContentType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AllUsersServlet extends BaseServlet {
    private final UserService userService;
    private final Gson gson;

    public AllUsersServlet(UserService userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.getName());
        response
            .getOutputStream()
            .print(gson.toJson(userService.allUsers()))
        ;
    }
}
