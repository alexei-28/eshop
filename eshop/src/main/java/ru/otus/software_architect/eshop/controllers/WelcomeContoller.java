package ru.otus.software_architect.eshop.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeContoller {
    @Value("${spring.application.name}")
    private String appName;
    @Value("${spring.application.version}")
    private String appVersion;

    @GetMapping("/welcome")
    public String welcome(Model model) {
        model.addAttribute("appNameAndVersion", appName + " " + "ver." + appVersion);
        return "welcome";
    }
}
