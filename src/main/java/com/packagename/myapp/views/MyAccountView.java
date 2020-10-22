package com.packagename.myapp.views;

import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.User;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;

@Route(value = "account", layout = MainLayout.class)
@PageTitle("My account")
@CssImport("./styles/shared-styles.css")
public class MyAccountView extends VerticalLayout {

    private final UserRepository userRepository;
    private final LoginService loginService;

    private final Binder<User> binder = new BeanValidationBinder<>(User.class);

    public MyAccountView(UserRepository userRepository, LoginService loginService) {
        this.userRepository = userRepository;
        this.loginService = loginService;
    }

    @PostConstruct
    private void init() {
        add(new H3("My Account"));
        createForm();
    }

    private void createForm() {

        VerticalLayout accountForm = new VerticalLayout();

        //private data

        binder.setBean(loginService.getAuthenticatedUser());

        //read-only data
        TextField first = new TextField("First Name");
        first.addThemeName("bordered");
        first.addClassName("vaadin-text-field-styles.css");
        binder.forField(first)
                .bind(User::getName, null);

        TextField last = new TextField("Last Name");
        last.addThemeName("bordered");
        last.addClassName("vaadin-text-field-styles.css");
        binder.forField(last)
                .bind(User::getSurname, null);

        TextField cnp = new TextField("CNP");
        cnp.addThemeName("bordered");
        cnp.addClassName("vaadin-text-field-styles.css");
        binder.forField(cnp)
                .bind(User::getCnp, null);

        TextField birthday = new TextField("Birth Day");
        birthday.addThemeName("bordered");
        birthday.addClassName("vaadin-text-field-styles.css");
        binder.forField(birthday)
                .bind(User::getBirthDay, null);

        TextField username = new TextField("Username");
        username.addThemeName("bordered");
        username.addClassName("vaadin-text-field-styles.css");
        binder.forField(username)
                .bind(User::getUsername, null);

        //editable data
        EmailField email = new EmailField("Email");
        email.addThemeName("bordered");
        email.addClassName("vaadin-text-field-styles.css");

        TextField phone = new TextField("Phone number");
        phone.addThemeName("bordered");
        phone.addClassName("vaadin-text-field-styles.css");

        TextField address = new TextField("Address");
        address.addThemeName("bordered");
        address.addClassName("vaadin-text-field-styles.css");

        PasswordField password = new PasswordField("Password");
        password.addThemeName("bordered");
        password.addClassName("vaadin-text-field-styles.css");

        binder.bind(email, "email");
        binder.bind(phone, "phoneNumber");
        binder.bind(address, "address");

        binder.forField(password)
                .withValidator(pass -> pass.matches("^(|(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})$"),
                        "need 6 or more chars, mixing digits, lowercase and uppercase letters")
                .bind(User::getPassword, User::setPassword);

        Button save = new Button("Save", e -> {
            User user = binder.getBean();

            userRepository.save(user);
        });

        accountForm.add(username, first, last, birthday, email, phone, address, password, save);

        add(accountForm);
    }

}