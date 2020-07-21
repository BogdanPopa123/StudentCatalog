package com.packagename.myapp.services;

import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.User;
import com.packagename.myapp.models.UserRole;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final CookieService cookieService;

    public LoginService(UserRepository userRepository, CookieService cookieService) {
        this.userRepository = userRepository;
        this.cookieService = cookieService;
    }

    public boolean login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            Notification.show("Introduce login / password");
            return false;
        }

        User authUser = userRepository.findByUsername(username);

        if (authUser == null) {
            Notification.show("Username not found");
            return false;
        }

        if (!authUser.getPassword().equals(HashingService.hashThis(password))) {
            Notification.show("Username or password is wrong. Please try again!");
            return false;
        }

        cookieService.addUserCookie(authUser);
        Notification.show("Login successful");
        return true;
    }

    public void logout() {
        cookieService.setAnonymousUser();
    }

    public boolean registerNewUser(User user) {
        user.setRole(UserRole.STUDENT);

        String password = user.getPassword();
        user.setPassword(HashingService.hashThis(user.getPassword()));

        userRepository.save(user);

        return login(user.getUsername(), password);
    }

    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkUsername(String password) {
        return userRepository.existsByUsername(password);
    }

    public User getAuthenticatedUser() {
        return cookieService.getCurrentUserFromCookies();
    }

    public boolean checkAuth() {
        User authUser = getAuthenticatedUser();

        return authUser != null && !authUser.checkAnonymous();
    }
}
