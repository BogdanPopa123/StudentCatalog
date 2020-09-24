package com.packagename.myapp.views;

import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layouts.MainLayout;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;

@Route(value = "account", layout = MainLayout.class)
@PageTitle("My account")
@CssImport("./styles/shared-styles.css")
public class MyAccountView extends VerticalLayoutAuthRestricted {
    public MyAccountView(LoginService loginService) {
        super(loginService);
    }

    @PostConstruct
    private void init() {
        add(new H5("My Account"));
        add(new TextField("Text"));
    }
}
