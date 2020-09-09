package com.packagename.myapp.views.layouts;

import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.services.NotificationService;
import com.packagename.myapp.views.HomeView;
import com.packagename.myapp.views.LoginView;
import com.packagename.myapp.views.RegisterView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class VerticalLayoutAuthRestricted extends VerticalLayout {

    private final LoginService loginService;

    @Autowired
    private NotificationService notificationService;

    public VerticalLayoutAuthRestricted(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (loginService.isAuthenticated()) {
            if (this instanceof LoginView || this instanceof RegisterView) {
                UI.getCurrent().navigate(HomeView.class);
                notificationService.success("Login successful");
            }
        } else {
            UI.getCurrent().navigate(LoginView.class);
        }
    }

}
