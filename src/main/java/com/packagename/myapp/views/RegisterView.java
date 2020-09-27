package com.packagename.myapp.views;

import com.packagename.myapp.models.User;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;

@Route(value = "register")
@CssImport("./styles/register-view-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class RegisterView extends VerticalLayoutAuthRestricted {

    private static final Logger logger = LogManager.getLogger(RegisterView.class);

    private final LoginService loginService;
    private final Binder<User> binder = new BeanValidationBinder<>(User.class);
    private TextField username = new TextField("Username");
    private TextField email = new TextField("Email");
    private PasswordField password = new PasswordField("Password");
    private PasswordField confirmPassword = new PasswordField("Confirm Password");
    private TextField name = new TextField("First Name");
    private TextField surname = new TextField("Surname");
    private TextField phoneNumber = new TextField("Phone Number");
    private TextField birthDay = new TextField("Birth Date");
    private User user = new User();

    public RegisterView(LoginService loginService) {
        super(loginService);
        this.loginService = loginService;
    }

    @PostConstruct
    private void init() {
        addClassName("register-view-form-style");

        H2 header = new H2("Register");
        header.addClassName("register-header-style");

        addRegisterForm();

        setBinder();
    }

    private void addRegisterForm() {
        username.addClassName("register-field-style");
        username.addThemeName("bordered");
        username.setRequired(true);
        username.setMaxLength(50);

        email.addClassName("register-field-style");
        email.addThemeName("bordered");
        email.setRequired(true);
        email.setMaxLength(50);

        password.addClassName("register-field-style");
        password.addThemeName("bordered");
        password.setRequired(true);
        password.setMaxLength(50);

        confirmPassword.addClassName("register-field-style");
        confirmPassword.addThemeName("bordered");
        confirmPassword.setRequired(true);
        confirmPassword.setMaxLength(50);

        name.addClassName("register-field-style");
        name.addThemeName("bordered");
        name.setRequired(true);
        name.setMaxLength(50);

        surname.addClassName("register-field-style");
        surname.addThemeName("bordered");
        surname.setRequired(true);
        surname.setMaxLength(50);

        phoneNumber.addClassName("register-field-style");
        phoneNumber.addThemeName("bordered");
//        phoneNumberField.setRequired(true);
        phoneNumber.setMaxLength(50);

        birthDay.addClassName("register-field-style");
        birthDay.addThemeName("bordered");
//        birthDay.addClassNames("birth-date-style", "register-field-style");
//        birthDay.setRequired(true);
        birthDay.setMaxLength(50);


        VerticalLayout firstColumn = new VerticalLayout(username, email, password, confirmPassword);
        VerticalLayout secondColumn = new VerticalLayout(name, surname, phoneNumber, birthDay);

        firstColumn.addClassName("register-column-style");
        secondColumn.addClassName("register-column-style");

        HorizontalLayout userData = new HorizontalLayout(firstColumn, secondColumn);
        userData.addClassName("register-column-style");

        Button registerButton = new Button("Register");
        registerButton.addClassName("register-user-button-style");
        registerButton.addClickListener(this::register);
        registerButton.addClickShortcut(Key.ENTER);

        Button loginButton = new Button("Back to login page");
        loginButton.addClassName("login-back-button-style");
        loginButton.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));

        VerticalLayout registerForm = new VerticalLayout(userData, registerButton, loginButton);
        registerForm.addClassName("register-form-style");

        add(registerForm);
    }

    private void setBinder() {
        binder.setBean(user);
        binder.bindInstanceFields(this);

        binder.forField(password)
                .withValidator(text -> text.equals(confirmPassword.getValue()), "The specified password do not match.")
                .bind(User::getPassword, User::setPassword);

        binder.forField(email)
                .withValidator(new EmailValidator("Not a valid email address."))
                .withValidator(text -> !loginService.checkEmail(text), "This email already exists.")
                .bind(User::getEmail, User::setEmail);

        binder.forField(username)
                .withValidator(text -> !loginService.checkUsername(text), "The username already exists.")
                .bind(User::getUsername, User::setUsername);
    }

    private void register(ClickEvent<Button> e) {
        if (binder.isValid()) {
            user = binder.getBean();

            logger.debug("Sending register data");
            if (loginService.registerNewUser(user)) {
                UI.getCurrent().getPage().reload();
            }
        }else{
            logger.debug("Not valid register data");
        }
    }

}
