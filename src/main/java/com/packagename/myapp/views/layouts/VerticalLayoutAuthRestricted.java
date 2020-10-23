package com.packagename.myapp.views.layouts;

import com.packagename.myapp.Application;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.services.NotificationService;
import com.packagename.myapp.views.HomeView;
import com.packagename.myapp.views.LoginView;
import com.packagename.myapp.views.RegisterView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class VerticalLayoutAuthRestricted extends VerticalLayout {

    protected final LoginService loginService;

    private final NotificationService notificationService;

    public VerticalLayoutAuthRestricted() {
        notificationService = NotificationService.getService();
        loginService = Application.context.getBean(LoginService.class);
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
