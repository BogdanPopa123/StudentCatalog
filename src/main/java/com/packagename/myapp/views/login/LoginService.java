package com.packagename.myapp.views.login;

import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository userLoginDAO;

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

        return "Login successful";
    }
}
