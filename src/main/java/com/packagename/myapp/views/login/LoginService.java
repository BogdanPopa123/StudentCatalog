package com.packagename.myapp.views.login;

import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.User;
import com.packagename.myapp.views.home.HomeView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userLoginDAO;

    public LoginService(UserRepository userLoginDAO) {
        this.userLoginDAO = userLoginDAO;
    }

    public String login(String username, String password) {

        if (username.isEmpty() || password.isEmpty()) {
            return "Introduce login / password";
        }

        User authUser = userLoginDAO.findByUsername(username);

        if (authUser == null) {
            return "Username not found";
        }

        if (!authUser.getPassword().equals(HashingService.hashThis(password))) {
            return "Username or password is wrong. Please try again!";
        }

        VaadinSession session = VaadinSession.getCurrent();
        session.setAttribute("user", authUser);

        UI.getCurrent().navigate(HomeView.class);
        return "Login successful";
    }
}
