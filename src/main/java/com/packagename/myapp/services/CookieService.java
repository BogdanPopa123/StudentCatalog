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


        if (cookies != null) {
            logger.trace("Trying to get cookie: " + name);
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    logger.trace("Found cookie " + name + " : " + cookie.getValue());
                    return cookie;
                }
            }
        }

        logger.warn("Failed to find cookie: " + name);
        return null;
    }


    public User getCurrentUserFromCookies() {
        Cookie userCookie = getCookieByName("User");

        if (userCookie != null) {
            return User.jsonParse(userCookie.getValue());
        }

        logger.debug("User cookie not found");
        logger.debug("Set anonymous User");

        return setAnonymousUser();
    }

    public User addUserCookie(User authUser) {
        Cookie userCookie = new Cookie("User", authUser.toJSON());
        userCookie.setMaxAge(604800);
        userCookie.setPath(VaadinService.getCurrentRequest().getContextPath());

        logger.debug("Update User auth cookie data");
        VaadinService.getCurrentResponse().addCookie(userCookie);

        return authUser;
    }

    public User setAnonymousUser() {
        logger.debug("Set anonymous User to cookies");
        return addUserCookie(User.getAnonymousUser());
    }
}


