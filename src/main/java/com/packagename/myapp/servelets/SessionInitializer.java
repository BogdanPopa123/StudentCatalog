package com.packagename.myapp.servelets;

import com.packagename.myapp.models.User;
import com.vaadin.flow.server.SessionDestroyEvent;
import com.vaadin.flow.server.SessionInitEvent;
import com.vaadin.flow.server.VaadinServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

//@Component
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
