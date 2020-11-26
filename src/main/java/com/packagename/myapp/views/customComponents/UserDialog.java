package com.packagename.myapp.views.customComponents;

import com.packagename.myapp.Application;
import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.User;
import com.packagename.myapp.models.UserRole;
import com.packagename.myapp.views.customComponents.manageButtons.ModifyDialog;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;

public abstract class UserDialog {

    private static final UserRepository userRepository = Application.context.getBean(UserRepository.class);

    public static <T extends User> void addUserFieldsToManageButtons(ModifyDialog<T> modifyDialog) {
        Binder<T> binder = modifyDialog.getBinder();

        TextField username = new TextField("Username");
        EmailField email = new EmailField("Email");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm your password");
        TextField surname = new TextField("Surname");
//        DatePicker birthDay = new DatePicker("Date of birth");
        TextField cnp = new TextField("CNP");
        TextField address = new TextField("Address");
        TextField phoneNumber = new TextField("Phone Number");

        ListBox<UserRole> userRole = new ListBox<>();
        userRole.setItems(UserRole.TEACHER, UserRole.STUDENT);
        userRole.setValue(UserRole.STUDENT);
        RadioButtonGroup<Boolean> isAdmin = new RadioButtonGroup<>();
        isAdmin.setLabel("Give this user Admin privileges?");
        isAdmin.setItems(true, false);
        isAdmin.setValue(false);


        binder.forField(username)
                .asRequired("Enter a name")
                .withValidator(username1 -> !userRepository.existsByUsername(username1), "Not a valid username!")
                .bind(T::getUsername, T::setUsername);

        binder.forField(surname)
                .asRequired("Enter a surname")
                .bind(T::getSurname, T::setSurname);

        binder.forField(phoneNumber)
                .asRequired("Enter a phone number")
                .bind(T::getUsername, T::setUsername);

        binder.forField(email)
                .asRequired("Enter an email")
                .withValidator(new EmailValidator("not an email"))
                .bind(T::getEmail, T::setEmail);

        binder.forField(password)
                .asRequired("Enter a password")
                .bind(T::getPassword, T::setPassword);

        binder.forField(confirmPassword)
                .asRequired("Confirm password")
                .withValidator(p -> p.equals(password.getValue()), "Password does not match");

        binder.forField(cnp)
                .asRequired("Enter a CNP")
                .bind(T::getCnp, T::setCnp);

        binder.forField(address)
                .asRequired("Enter an address")
                .bind(T::getAddress, T::setAddress);

        binder.forField(userRole)
                //   .asRequired("Enter a role")
                .bind(T::getRole, T::setRole);

        binder.forField(isAdmin)
                .bind(T::isAdmin, T::setAdmin);


        modifyDialog.addFields(new HorizontalLayout(
                new VerticalLayout(username, email, surname, phoneNumber, cnp, address),
                new VerticalLayout(password, confirmPassword, userRole, isAdmin)));
//                new VerticalLayout(password, confirmPassword, birthDay)));

//                binder.bindInstanceFields(modifyDialog);
    }
}
