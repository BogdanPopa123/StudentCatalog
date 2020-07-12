package com.packagename.myapp.views.login;

import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.home.HomeView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import javax.annotation.PostConstruct;

@Route("login")
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@CssImport("./styles/login-view-styles.css")
public class LoginView extends VerticalLayout {

    private final LoginService loginService;

    public LoginView(LoginService loginService) {
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
        usernameField.setMaxLength(50);

        PasswordField passwordField = new PasswordField("Password");
        passwordField.addClassName("password-style");
        passwordField.addThemeName("bordered");
        passwordField.setMaxLength(50);

        Button button = new Button("Login");
        button.addClassName("login-button-style");
        button.addClickListener(e -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            loginService.login(username, password);

            UI.getCurrent().getPage().reload();
            UI.getCurrent().navigate(HomeView.class);
        });

        button.addClickShortcut(Key.ENTER);

        loginForm.add(usernameField, passwordField, button);

        add(loginForm);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {

    }
}
