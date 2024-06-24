package ru.otus.software_architect.eshop.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.software_architect.eshop.service.UserService;

@Controller
public class IndexController {
    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/index")
    public String index(Model model) {
        String userName = UserService.getCurrentUserName();
        UserService.setUserName(userName);
        model.addAttribute("appName", appName);
        return "index";
    }
}
