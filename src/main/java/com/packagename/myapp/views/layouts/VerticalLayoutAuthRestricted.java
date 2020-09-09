package com.packagename.myapp.views.layouts;

import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.HomeView;
import com.packagename.myapp.views.LoginView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class VerticalLayoutAuthRestricted extends VerticalLayout {

    private final LoginService loginService;

    public VerticalLayoutAuthRestricted(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (!loginService.isAuthenticated()) {
            UI.getCurrent().navigate(LoginView.class);
        }
    }

}
