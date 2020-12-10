package com.packagename.myapp.services;

import com.google.common.base.Strings;
import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.User;
import com.packagename.myapp.models.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

    private static final Logger logger = LogManager.getLogger(LoginService.class);

    private final UserRepository userRepository;
    private final CookieService cookieService;
    private final NotificationService notificationService;

    public LoginService(UserRepository userRepository, CookieService cookieService, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.cookieService = cookieService;
        this.notificationService = notificationService;
    }

    public boolean login(String username, String password) {
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
            return notifyStatus("Insert login / password", false);
        }

        if (!checkUsername(username)) {
            return notifyStatus("Username not found", false);
        }

        if (!checkUserPassword(username, password)) {
            return notifyStatus("Username or password is wrong. Please try again!", false);
        }

        User authUser = userRepository.findByUsername(username);
        cookieService.addUserCookie(authUser);

        return notifyStatus("Login successful", true);
    }

    private boolean notifyStatus(String message, boolean status) {
        logger.debug(message);

        if (status) {
            notificationService.success(message);
        } else {
            notificationService.alert(message);
        }

        return status;
    }

    public User logout() {
        logger.debug("Logout User: " + getAuthenticatedUser().getUsername());
        return cookieService.setAnonymousUser();
    }

    public boolean registerNewUser(User user) {
        user.setRole(UserRole.STUDENT);

        String password = user.getPassword();
        user.setPassword(HashingService.hashThis(user.getPassword()));

        logger.info("Registering new User: " + user.toString());
        userRepository.save(user);

        return login(user.getUsername(), password);
    }

    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkUserPassword(String username, String password) {
        String hashedPassword = HashingService.hashThis(password);

        return userRepository.existsByUsernameAndPassword(username, hashedPassword);
    }

    public User getAuthenticatedUser() {
        logger.trace("Trying to get authenticated User");


        User user = cookieService.getCurrentUserFromCookies();

        if (checkUserValidity(user)) {
            return user;
        }

        logger.info("Found not valid User from cookies: " + user);
        return cookieService.setAnonymousUser();
    }

    public boolean isAuthenticated() {
        User authUser = getAuthenticatedUser();

        return authUser.getUsername() != null && !authUser.checkAnonymous();
    }

    public boolean checkUserValidity(User user) {
        return user != null && (user.checkAnonymous() || userRepository.existsById(user.getId()));
    }
}
