package com.packagename.myapp.servelets;

import com.packagename.myapp.models.User;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.*;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet(asyncSupported = true, urlPatterns = "/*")
public class SessionInitializer extends VaadinServlet {

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionInitListener(this::sessionInit);
        getService().addSessionDestroyListener(this::sessionDestroy);

    }

    public void sessionInit(SessionInitEvent event) {
        User anonymousUser = new User();
        anonymousUser.setUsername("AnonymousUser");
        event.getSession().setAttribute("user", anonymousUser);
    }

    public void sessionDestroy(SessionDestroyEvent event) {

    }

}
