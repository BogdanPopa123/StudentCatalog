package com.packagename.myapp.views.login;

import com.packagename.myapp.views.main.MainUserView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

@Route("")
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
@CssImport("./styles/login-view-styles.css")
public class LoginView extends VerticalLayout {

    public LoginView(LoginService service) {
        addClassName("main-view-form-style");

        VerticalLayout loginForm = new VerticalLayout();
        loginForm.addClassName("login-form-style");
        // Use TextField for standard text input
        TextField usernameField = new TextField("Username");
        usernameField.addThemeName("bordered");
        usernameField.setMaxLength(50);
        usernameField.addClassName("username-style");

        PasswordField passwordField = new PasswordField("Password");
        passwordField.addThemeName("bordered");
        passwordField.setMaxLength(50);
        passwordField.addClassName("password-style");

        Button button = new Button("Login");
        button.addClickListener(e -> {
            Notification.show(service.login(usernameField.getValue(), passwordField.getValue()));
            //UI.getCurrent().navigate(MainUserView.class);
        });
        button.addClassName("login-button-style");

        button.addClickShortcut(Key.ENTER);

        loginForm.add(usernameField,passwordField,button);
        add(loginForm);

//        // Button click listeners can be defined as lambda expressions
//        Button button = new Button("Say hello",
//                e -> Notification.show(service.greet(textField.getValue())));
//
//        // Theme variants give you predefined extra styles for components.
//        // Example: Primary button has a more prominent look.
//        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//
//        // You can specify keyboard shortcuts for buttons.
//        // Example: Pressing enter in this view clicks the Button.
//        button.addClickShortcut(Key.ENTER);
//
//        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
//        addClassName("centered-content");
//
//        add(textField, button);
    }
}
