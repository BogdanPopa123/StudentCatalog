package com.packagename.myapp.views;

import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layout.MainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home")
@CssImport("./styles/shared-styles.css")
public class HomeView extends VerticalLayout {

    private final LoginService loginService;

    public HomeView(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostConstruct
    private void init() {
        add(new H5(loginService.getAuthenticatedUser().toString()));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (!loginService.checkAuth()) {
            UI.getCurrent().navigate(LoginView.class);
        }
    }


}