package com.packagename.myapp.views;

import com.packagename.myapp.models.User;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;

@Route("login")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@CssImport("./styles/login-view-styles.css")
public class LoginView extends VerticalLayoutAuthRestricted {

    private static final Logger logger = LogManager.getLogger(LoginView.class);

    private final LoginService loginService;
    private final TextField username = new TextField("Username");
    private final PasswordField password = new PasswordField("Password");

    private final Binder<User> binder = new BeanValidationBinder<>(User.class);

    private User user = new User();

    public LoginView(LoginService loginService) {
        super(loginService);
        this.loginService = loginService;
    }

    @PostConstruct
    private void init() {
        addClassName("main-view-form-style");

        addLoginForm();

        setupBinder();
    }


    private void addLoginForm() {
        VerticalLayout loginForm = new VerticalLayout();
        loginForm.addClassName("login-form-style");

        username.addClassName("username-style");
        username.addThemeName("bordered");
//        username.setRequired(true);
        username.setMaxLength(50);

        password.addClassName("password-style");
        password.addThemeName("bordered");
//        password.setRequired(true);
        password.setMaxLength(50);

        Button loginButton = new Button("Login");
        loginButton.addClassName("login-button-style");

        loginButton.addClickListener(this::loginClickEvent);

        loginButton.addClickShortcut(Key.ENTER);

        Button registerButton = new Button("Register");
        registerButton.addClassName("register-button-style");
        registerButton.addClickListener(e -> UI.getCurrent().navigate(RegisterView.class));

        loginForm.add(username, password, loginButton, registerButton);

        add(loginForm);
    }


    private void setupBinder() {
        binder.setBean(user);

        binder.forField(username)
                .asRequired("Enter username")
//                .withValidator(username -> !Strings.isNullOrEmpty(username), "Enter username")
                .withValidator(loginService::checkUsername, "Username not registered")
                .bind(User::getUsername, User::setUsername);

        binder.forField(password)
                .asRequired("Enter password")
//                .withValidator(password -> !Strings.isNullOrEmpty(password), "Enter username")
                .withValidator(password -> loginService.checkUserPassword(username.getValue(), password), "Wrong login / password")
                .bind(User::getPassword, User::setPassword);

        binder.bindInstanceFields(this);
    }

    private void loginClickEvent(ClickEvent<Button> e) {
        logger.debug("Submit login form");
        if (binder.isValid()) {
            String username = this.username.getValue();
            String password = this.password.getValue();

            logger.debug("Send login request");
            if (loginService.login(username, password)) {
                UI.getCurrent().getPage().reload();
            }
        }else{
            logger.debug("Not valid login data");
        }
    }

}
