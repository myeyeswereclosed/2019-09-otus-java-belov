package ru.otus.hw15.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw15.domain.User;
import ru.otus.hw15.service.user.CachedUserService;
import ru.otus.hw15.service.user.UserService;
import java.util.List;

@Controller
public class HomeController {
    private UserService userService;

    @Autowired
    public HomeController(CachedUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    // to login see resources/data.sql
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String login(@RequestBody MultiValueMap<String, String> formData, Model model) {
        var user =
            userService.getByCredentials(
                formData.getFirst("login"),
                formData.getFirst("password")
            );

        return
            user
                .map(validUser -> resolvePage(validUser, model))
                .orElse("login_fail");
    }

    private String resolvePage(User user, Model model) {
        return
            user.isAdmin()
                ? getPage(model, userService.allUsers(), "admin")
                : getPage(model, userService.clients(), "client")
        ;
    }

    private String getPage(Model model, List<User> users, String template) {
        model.addAttribute("users", users);

        return template;
    }
}
