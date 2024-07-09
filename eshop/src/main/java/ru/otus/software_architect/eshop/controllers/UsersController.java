package ru.otus.software_architect.eshop.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.software_architect.eshop.model.Role;
import ru.otus.software_architect.eshop.model.User;
import ru.otus.software_architect.eshop.model.ViewMode;
import ru.otus.software_architect.eshop.repo.UserRepository;

import java.util.*;
/*-
    You can register a new user via login screen.
 */
@Controller
public class UsersController {
    @Value("${spring.application.name}")
    private String appName;
    private Iterable<User> usersList;
    private List<User> adminsList;
    private final static String ERROR_MESSAGE = "errorMessage";

    @Autowired
    private UserRepository userRepository;

    private static Logger logger = LogManager.getLogger(UsersController.class);

    // If class has only one constructor then @Autowired will execute automatically
    public UsersController(UserRepository userRepository) {
        logger.info("UsersController:");
        this.userRepository = userRepository;
        if (userRepository.findByUsername("admin@admin.com") == null) {
            logger.info("UsersController: create default admin user: admin@admin.com");
            // Create admin user
            User user = new User();
            user.setUsername("admin@admin.com");
            user.setPassword("admin@admin.com");
            user.setCreated(new Date());
            user.setActive(true);
            user.setRoles(new HashSet<>(Arrays.asList(Role.ADMIN)));
            userRepository.save(user);
        }
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        logger.info("getAllUsers: model = " + model);
        if (model.getAttribute(ERROR_MESSAGE) == null) {
            logger.info("getAllUsers: load_data");
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(Role.ADMIN);
            adminsList = userRepository.findAllByRolesIn(roleSet);
            usersList = userRepository.findAll();
        }
        model.addAttribute("usersList", usersList);
        model.addAttribute("adminsList", adminsList);
        model.addAttribute("appName", appName);
        return "users";
    }

    /*-
    @RequestMapping("user/add")
    public String addtUser(Model model) {
        logger.info("addtUser");
        model.addAttribute("isAdd", true);
        model.addAttribute("category", new Category());
        model.addAttribute("title", "Add Category");
        model.addAttribute("viewMode", ViewMode.ADD);
        return "category";
    }

     */

    @RequestMapping("user/view/{id}")
    public String viewUser(@PathVariable("id") int id, Model model) {
        Optional<User> user = userRepository.findById(id);
        logger.info("find_user = " + user);
        model.addAttribute("isView", true);
        model.addAttribute("user", user);
        model.addAttribute("title", "View User");
        return "user";
    }

    @RequestMapping("user/edit/{id}")
    public String editCategory(@PathVariable("id") int id, Model model) {
        Optional<User> user = userRepository.findById(id);
        logger.info("find_user = " + user);
        Optional<User> userAdmin = adminsList.stream()
                .filter(it -> it.getId() == id)
                .findFirst();
        boolean isActiveHide = false;
        if (userAdmin.isPresent() && adminsList.size() == 1) {
            isActiveHide = true;
        }
        logger.info("isActiveHide = " + isActiveHide);
        model.addAttribute("user", user);
        model.addAttribute("title", "Edit User");
        model.addAttribute("viewMode", ViewMode.EDIT);
        model.addAttribute("isActiveHide", isActiveHide);
        return "user";
    }

    @RequestMapping("user/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        logger.info("deleteUser: user_id_to_delete = " + id);
        Optional<User> userAdmin = adminsList.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
        if (userAdmin.isPresent() && adminsList.size() == 1) {
            String errorMessage = "Must have at least one administrator";
            logger.error(errorMessage);
            // add to param "model" of method "getAllUsers()"
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, errorMessage);
        } else {
            userRepository.deleteById(id);
        }
        return "redirect:/users";
    }

    @PostMapping(value = "/user")
    public String submitUser(User user, Model model) {
        logger.info("submitUser: user  = " + user);
        if (user.getId() == 0) { // add category
            user.setCreated(new Date());
        } else { // update category
            user.setUpdated(new Date());
        }
        userRepository.save(user);
        return "redirect:/users";
    }
}