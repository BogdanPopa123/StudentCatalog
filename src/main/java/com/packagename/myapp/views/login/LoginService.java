package com.packagename.myapp.views.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.User;
import com.packagename.myapp.views.home.HomeView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public class LoginService {

    private final UserRepository userLoginDAO;

    public LoginService(UserRepository userLoginDAO) {
        this.userLoginDAO = userLoginDAO;

//
//        User anonymousUser = new User();
//        anonymousUser.setUsername("AnonymousUser");
//        VaadinSession session = VaadinSession.getCurrent();
//        session.setAttribute("user", anonymousUser);
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


        ObjectMapper mapper = new ObjectMapper();
        String jsonUser = "";
        try {
            jsonUser = mapper.writeValueAsString(authUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Cookie userCookie = new Cookie("user", jsonUser);

        userCookie.setMaxAge(400000);
        userCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
        VaadinService.getCurrentResponse().addCookie(userCookie);

        UI.getCurrent().navigate(HomeView.class);
        return "Login successful";
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        return (serverFactory) -> serverFactory.addContextCustomizers(
                (context) -> context.setCookieProcessor(new LegacyCookieProcessor()));
    }
}
