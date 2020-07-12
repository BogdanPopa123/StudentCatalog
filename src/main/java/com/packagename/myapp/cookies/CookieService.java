package com.packagename.myapp.cookies;

import com.packagename.myapp.models.User;
import com.vaadin.flow.server.VaadinService;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public class CookieService {

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

        User anonymousUser = new User();
        anonymousUser.setUsername("AnonymousUser");
        addUserCookie(anonymousUser);

        return anonymousUser;
    }

    public void addUserCookie(User authUser) {
        Cookie userCookie = new Cookie("user", authUser.toJSON());
        userCookie.setMaxAge(604800);
        userCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
        VaadinService.getCurrentResponse().addCookie(userCookie);
    }


}
