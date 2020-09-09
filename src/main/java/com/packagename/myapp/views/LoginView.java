package com.packagename.myapp.views;

import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;

@Route("login")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@CssImport("./styles/login-view-styles.css")
public class LoginView extends VerticalLayoutAuthRestricted {

    private final LoginService loginService;

    public LoginView(LoginService loginService) {
        super(loginService);
        this.loginService = loginService;
    }

    @PostConstruct
    private void init() {

        addClassName("main-view-form-style");

        VerticalLayout loginForm = new VerticalLayout();
        loginForm.addClassName("login-form-style");

        TextField usernameField = new TextField("Username");
        usernameField.addClassName("username-style");
        usernameField.addThemeName("bordered");
        usernameField.setRequired(true);
        usernameField.setMaxLength(50);

        PasswordField passwordField = new PasswordField("Password");
        passwordField.addClassName("password-style");
        passwordField.addThemeName("bordered");
        passwordField.setRequired(true);
        passwordField.setMaxLength(50);

        Button loginButton = new Button("Login");
        loginButton.addClassName("login-button-style");
        loginButton.addClickListener(e -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            if (loginService.login(username, password)) {
                UI.getCurrent().getPage().reload();
            }
        });
        loginButton.addClickShortcut(Key.ENTER);

        Button registerButton = new Button("Register");
        registerButton.addClassName("register-button-style");
        registerButton.addClickListener(e -> UI.getCurrent().navigate(RegisterView.class));


        loginForm.add(usernameField, passwordField, loginButton, registerButton);

        add(loginForm);
    }

}
