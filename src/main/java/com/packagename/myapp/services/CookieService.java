package com.packagename.myapp.services;

import com.packagename.myapp.models.User;
import com.vaadin.flow.server.VaadinService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public class CookieService {

    private static final Logger logger = LogManager.getLogger(CookieService.class);

    public Cookie getCookieByName(String name) {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }


    public User getCurrentUserFromCookies() {
        Cookie userCookie = getCookieByName("user");

        if (userCookie != null) {
            return User.jsonParse(userCookie.getValue());
        }
        setAnonymousUser();

        return User.getAnonymousUser();
    }

    public void addUserCookie(User authUser) {
        Cookie userCookie = new Cookie("user", authUser.toJSON());
        userCookie.setMaxAge(604800);
        userCookie.setPath(VaadinService.getCurrentRequest().getContextPath());

        logger.info("Update user auth cookie data");
        VaadinService.getCurrentResponse().addCookie(userCookie);
    }

    public void setAnonymousUser() {
        addUserCookie(User.getAnonymousUser());
    }
}
