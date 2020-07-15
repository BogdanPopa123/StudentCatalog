package com.packagename.myapp.views;

import com.packagename.myapp.services.LoginService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;

@Route(value = "register")
@CssImport("./styles/register-view-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class RegisterView extends VerticalLayout {

    private final LoginService loginService;

    public RegisterView(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostConstruct
    private void ini() {
        addClassName("register-view-form-style");

        H2 header = new H2("Register");
        header.addClassName("register-header-style");

        TextField usernameField = new TextField("Username");
        usernameField.addClassName("register-field-style");
        usernameField.addThemeName("bordered");
        usernameField.setMaxLength(50);

        TextField emailField = new TextField("Username");
        emailField.addClassName("register-field-style");
        emailField.addThemeName("bordered");
        emailField.setMaxLength(50);

        PasswordField passwordField = new PasswordField("Password");
        passwordField.addClassName("register-field-style");
        passwordField.addThemeName("bordered");
        passwordField.setMaxLength(50);

        PasswordField confirmPasswordField = new PasswordField("Confirm Password");
        confirmPasswordField.addClassName("register-field-style");
        confirmPasswordField.addThemeName("bordered");
        confirmPasswordField.setMaxLength(50);

        TextField nameField = new TextField("First Name");
        nameField.addClassName("register-field-style");
        nameField.addThemeName("bordered");
        nameField.setMaxLength(50);

        TextField surnameField = new TextField("Surname");
        surnameField.addClassName("register-field-style");
        surnameField.addThemeName("bordered");
        surnameField.setMaxLength(50);

        TextField phoneNumberField = new TextField("Phone Number");
        phoneNumberField.addClassName("register-field-style");
        phoneNumberField.addThemeName("bordered");
        phoneNumberField.setMaxLength(50);

        DatePicker birthDateField = new DatePicker("Birth Date");
        birthDateField.addClassNames("birth-date-style", "register-field-style");

        VerticalLayout firstColumn = new VerticalLayout(usernameField, emailField, passwordField, confirmPasswordField);
        VerticalLayout secondColumn = new VerticalLayout(nameField, surnameField, phoneNumberField, birthDateField);

        firstColumn.addClassName("register-column-style");
        secondColumn.addClassName("register-column-style");

        HorizontalLayout userData = new HorizontalLayout(firstColumn, secondColumn);
        userData.addClassName("register-column-style");

        Button registerButton = new Button("Register");
        registerButton.addClassName("register-user-button-style");
        registerButton.addClickListener(e -> {
            UI.getCurrent().getPage().reload();
        });
        registerButton.addClickShortcut(Key.ENTER);

        Button loginButton = new Button("Back to login page");
        loginButton.addClassName("login-back-button-style");
        loginButton.addClickListener(e -> {
            UI.getCurrent().navigate(LoginView.class);
        });

        VerticalLayout registerForm = new VerticalLayout(userData, registerButton, loginButton);
        registerForm.addClassName("register-form-style");

        add(registerForm);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (loginService.checkAuth()) {
            UI.getCurrent().navigate(HomeView.class);
        }
    }
}
