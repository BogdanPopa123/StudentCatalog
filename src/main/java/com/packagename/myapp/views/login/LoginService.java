package com.packagename.myapp.views.login;

import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.User;
import com.packagename.myapp.views.main.MainUserView;
import com.vaadin.flow.component.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userLoginDAO;

    public LoginService(UserRepository userLoginDAO) {
        this.userLoginDAO = userLoginDAO;
    }

    public String login(String username, String password) {

        if(username.isEmpty() || password.isEmpty()){
            return "Introduce login / password";
        }

        User authUser = userLoginDAO.findByUsername(username);

        if (authUser == null) {
            return "Username not found";
        }

        if (!authUser.getPassword().equals(password)) {
            return "Username or password is wrong. Please try again!";
        }

        UI.getCurrent().navigate(MainUserView.class);
        return "Login successful";
    }
}
