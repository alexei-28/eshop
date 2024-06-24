package ru.otus.software_architect.eshop.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @Value("${spring.application.name}")
    private String appName;

    private static Logger logger = LogManager.getLogger(LoginController.class);

    /*-
    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "Hello!";
    }
    */

    // Login form
    @RequestMapping("/login.html")
    public String login(Model model) {
        logger.info("open_login.html");
        model.addAttribute("appName", appName);
        return "login.html";
    }

    // Login form with error
    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("loginError", true);
        return "login.html";
    }

}