package ru.otus.software_architect.eshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.software_architect.eshop.service.UserService;

@Controller
public class NavigatorController {
    @GetMapping("/navigator")
    public String index(Model model) {
        model.addAttribute("username", UserService.getCurrentUserName());
        return "navigator";
    }
}
