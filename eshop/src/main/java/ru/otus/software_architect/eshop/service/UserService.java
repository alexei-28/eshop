package ru.otus.software_architect.eshop.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class UserService {
    private static String userName;

    public static String getCurrentUserName() {
        if (userName == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();
            return currentUser.getUsername(); // email
        } else {
            return userName;
        }
    }

    public static void setUserName(String newUserName) {
        userName = newUserName;
    }
}
