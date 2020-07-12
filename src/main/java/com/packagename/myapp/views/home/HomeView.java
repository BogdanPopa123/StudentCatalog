package com.packagename.myapp.views.home;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.packagename.myapp.models.User;
import com.packagename.myapp.views.login.LoginView;
import com.packagename.myapp.views.main.MainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home")
@CssImport("./styles/shared-styles.css")
public class HomeView extends VerticalLayout {

    public HomeView() {

    }

    @PostConstruct
    private void ini() {
        add(new H5("Text"));
        Cookie cookie = getCookieByName("user");

        ObjectMapper mapper = new ObjectMapper();

        User user = null;
        try {
            user = mapper.readValue(cookie.getValue(), User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        add(new H5(user.toString()));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        checkAuth();
    }

    protected boolean checkAuth() {
        Object user = VaadinSession.getCurrent().getAttribute("user");

        if (user == null) {
            UI.getCurrent().navigate(LoginView.class);
            return false;
        }
        return true;
    }

    private Cookie getCookieByName(String name) {
        // Fetch all cookies from the request
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

        // Iterate to find cookie by its name
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }
}