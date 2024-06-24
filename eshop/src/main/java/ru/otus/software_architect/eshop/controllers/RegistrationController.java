package ru.otus.software_architect.eshop.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.software_architect.eshop.model.Role;
import ru.otus.software_architect.eshop.model.User;
import ru.otus.software_architect.eshop.repo.UserRepository;
import ru.otus.software_architect.eshop.util.StringUtll;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Value("${spring.application.name}")
    private String appName;

    private static Logger logger = LogManager.getLogger(RegistrationController.class);

    @GetMapping("/registration.html")
    public String registration(Model model) {
        logger.info("registration, model = " + model);
        model.addAttribute("appName", appName);
        return "registration.html";
    }

    @PostMapping("/registration.html")
    // Pass class "User" because User's column names are same as your name which pass from html.
    // Also because you use "User" as a param 
    public String registartionNewUser(User user, Model model, String retypePassword) {
        logger.info("registartionNewUser, new_user = " + user);
        try {
            checkFields(user, retypePassword);

            user.setActive(true);
            user.setRoles(new HashSet<>(Arrays.asList(Role.ADMIN)));
            user.setCreated(new Date());

            userRepository.save(user);
            return "redirect:/login.html";
        } catch (Exception ex) {
            logger.error("registartionNewUser: Error = " + ex.getMessage(), ex);
            model.addAttribute("registrationError", ex.getMessage());
            return "registration.html";
        }
    }

    private void checkFields(User user, String retypePassword) throws InvalidParameterException {
        if (user.getUsername().trim().isEmpty()
                || user.getPassword().trim().isEmpty()
        ) {
            throw new InvalidParameterException("Аll fields are required");
        }
        if (!user.getPassword().equals(retypePassword)) {
            throw new InvalidParameterException("Passwords not equals");
        }
        if (!StringUtll.isValidEmail(user.getUsername())) {
            throw new InvalidParameterException("Invalid email");
        }
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            throw new InvalidParameterException("User already exist");
        }
    }
}
