package com.packagename.myapp.services;

import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.User;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userLoginDAO;
    private final CookieService cookieService;

    public LoginService(UserRepository userLoginDAO, CookieService cookieService) {
        this.userLoginDAO = userLoginDAO;
        this.cookieService = cookieService;
    }

    public boolean login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            Notification.show("Introduce login / password");
            return false;
        }

        User authUser = userLoginDAO.findByUsername(username);

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

    public User getAuthenticatedUser() {
        return cookieService.getCurrentUserFromCookies();
    }

    public boolean checkAuth() {
        User authUser = getAuthenticatedUser();

        return authUser != null && !authUser.checkAnonymous();
    }
}
