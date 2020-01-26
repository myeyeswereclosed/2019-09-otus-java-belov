package ru.otus.hw13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.hw13.domain.User;
import ru.otus.hw13.domain.UserRole;
import ru.otus.hw13.service.UserService;
import java.util.Arrays;

@Controller
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(@Qualifier("cachedUserService") UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin.html";
    }

    @GetMapping("/admin/users")
    public String userList(Model model) {
        model.addAttribute("users", userService.allUsers());

        return "userList.html";
    }

    @GetMapping("admin/user/create")
    public String createUserView(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Arrays.asList(UserRole.values()));

        return "userCreate.html";
    }

    @PostMapping("admin/user/create")
    public RedirectView createUser(@ModelAttribute User user) {
        userService.save(user);

        return new RedirectView("../../admin/users", false);
    }
}
