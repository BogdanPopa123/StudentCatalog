package com.packagename.myapp;

import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository userLoginDAO;

    public String login(String username, String password){

        String loginstatus="";
        User sm = null;
        sm = userLoginDAO.findByUsername(username);
        if(sm == null){
            loginstatus="Username not found";
            return loginstatus;
        }

        if(!sm.getPassword().equals(password)){
            loginstatus = "Username or password is wrong. Please try again!";
            return loginstatus;
        }
        loginstatus = "Login successful";
        return loginstatus;

    }
}
