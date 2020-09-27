package com.packagename.myapp.views.customComponents;

import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.User;
import com.packagename.myapp.models.UserRole;
import com.packagename.myapp.services.HashingService;
import com.packagename.myapp.services.LoginService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

public class UserForm extends Dialog {

    private final LoginService loginService;
    private final UserRepository userRepository;
    private User user;
    private Runnable onclose;

    public UserForm(LoginService loginService, UserRepository userRepository, User user, Runnable onclose) {
        this.loginService = loginService;
        this.userRepository = userRepository;
        this.user = user;
        this.onclose = onclose;
        init();
    }

    void init() {
        H2 title = new H2("Add a new user");
        TextField newUsernameField = new TextField("Username *", "username");
        EmailField newEmailField = new EmailField("Email", "email");
        TextField newNameField = new TextField("Name *", "name");
        TextField newSurnameField = new TextField("Surname *", "surname");
        TextField newPhoneNumber = new TextField("Phone Number", "phone number");
        PasswordField newPasswordField = new PasswordField("Password *", "password");
        PasswordField newConfirmField = new PasswordField("Confirm your password *", "confirm your password");
        DatePicker newDateOfBirthField = new DatePicker("Date of birth");
        ListBox<UserRole> newUserRoleListBox = new ListBox<>();
        newUserRoleListBox.setItems(UserRole.TEACHER, UserRole.STUDENT);
        newUserRoleListBox.setValue(UserRole.STUDENT);
        RadioButtonGroup<String> newUserIsAdmin = new RadioButtonGroup<>();
        newUserIsAdmin.setLabel("Give this user Admin privileges?");
        newUserIsAdmin.setItems("Yes", "No");
        newUserIsAdmin.setValue("No");


        Button addUserButton = new Button("Add user", event -> {

            user.setUsername(newUsernameField.getValue());
            user.setEmail(newEmailField.getValue());
            user.setName(newNameField.getValue());
            user.setSurname(newSurnameField.getValue());
            user.setPhoneNumber(newPhoneNumber.getValue());
            user.setPassword(HashingService.hashThis(newPasswordField.getValue()));
            user.setBirthDay(newDateOfBirthField.getValue().toString());
            if (newUserIsAdmin.getValue().equals("Yes")) {
                user.setAdmin(true);
            } else if (newUserIsAdmin.getValue().equals("No")) {
                user.setAdmin(false);
            }


            boolean isok = true;

            if (newUsernameField.getValue() == null || newUsernameField.getValue().trim().equals("") ||
                    newNameField.getValue() == null || newNameField.getValue().trim().equals("") ||
                    newSurnameField.getValue() == null || newSurnameField.getValue().trim().equals("") ||
                    newPasswordField.getValue() == null || newPasswordField.getValue().trim().equals("") ||
                    newConfirmField.getValue() == null || newConfirmField.getValue().trim().equals("") ||
                    newUserRoleListBox.getDataProvider() == null

            ) {
                //warningText.concat("Fields marked with * are mandatory\n");
                Notification.show("Fields marked with * are mandatory\n");
                isok = false;
            } else {
                if (!newPasswordField.getValue().equals(newConfirmField.getValue())) {
                    //warningText.concat("Your password fields don't match. Re-enter your password again.\n");
                    Notification.show("Your password fields don't match. Re-enter your password again.\n");
                    isok = false;
                }
                if (user.getEmail() != null && !user.getEmail().trim().equals("")) {
                    Boolean emailTestUser;
                    emailTestUser = userRepository.existsByEmail(user.getEmail());
                    if (emailTestUser == true && user.getEmail() != null) {
                        //warningText.concat("This email has already been used!\n");
                        Notification.show("This email has already been used!\n");
                        isok = false;
                    }
                }

                if (user.getPhoneNumber() != null && !user.getPhoneNumber().trim().equals("")) {
                    Boolean phoneNumberTestUser;
                    phoneNumberTestUser = userRepository.existsByPhoneNumber(user.getPhoneNumber());
                    if (phoneNumberTestUser == true) {
                        // warningText.concat("This phone number has already been used!\n");
                        Notification.show("This phone number has already been used!\n");
                        isok = false;
                    }
                }
                Boolean usernameTestUser;
                usernameTestUser = userRepository.existsByUsername(user.getUsername());
                if (usernameTestUser == true) {
                    //warningText.concat("This username has already been used!\n");
                    Notification.show("This username has already been used!\n");
                    isok = false;
                }
                if (newUserRoleListBox.getValue() == null) {
                    Notification.show("You must select a role");
                    isok = false;
                }

                if (isok) {
                    close();
                }
            }
        });
        addUserButton.addClickShortcut(Key.ENTER);

        Button cancelButton = new Button("Cancel", e -> {

        });

        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);

        HorizontalLayout row1 = new HorizontalLayout(title);
        HorizontalLayout row2 = new HorizontalLayout(newNameField, newSurnameField);
        HorizontalLayout row3 = new HorizontalLayout(newUsernameField, newEmailField);
        HorizontalLayout row4 = new HorizontalLayout(newPhoneNumber, newDateOfBirthField);
        HorizontalLayout row5 = new HorizontalLayout(newPasswordField, newConfirmField);
        HorizontalLayout row6 = new HorizontalLayout(newUserRoleListBox, newUserIsAdmin);
        HorizontalLayout row7 = new HorizontalLayout(addUserButton, new Button("Cancel", e -> {
            close();
        }));

        VerticalLayout userForm = new VerticalLayout();
        userForm.add(row1, row2, row3, row4, row5, row6, row7);

        add(userForm);
    }

    @Override
    public void close() {
        super.close();
        onclose.run();
    }
}
