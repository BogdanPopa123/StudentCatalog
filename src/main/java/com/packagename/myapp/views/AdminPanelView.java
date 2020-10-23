package com.packagename.myapp.views;


import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.User;
import com.packagename.myapp.models.UserRole;
import com.packagename.myapp.services.HashingService;
import com.packagename.myapp.views.layouts.MainLayout;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;

@Route(value = "admin", layout = MainLayout.class)
@PageTitle("AdminPanel")
@CssImport("./styles/shared-styles.css")
public class AdminPanelView extends VerticalLayoutAuthRestricted {

    private final UserRepository userRepository;

    public AdminPanelView(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void init() {

        if(loginService.getAuthenticatedUser().isAdmin() == true){
            TextField newUsernameField = new TextField("Username *", "username");
            EmailField newEmailField = new EmailField("Email", "email");
            TextField newNameField = new TextField("Name *", "name");
            TextField newSurnameField = new TextField("Surname *", "surname");
            TextField newPhoneNumber = new TextField("Phone Number", "phone number");
            PasswordField newPasswordField = new PasswordField("Password *", "password");
            PasswordField newConfirmField = new PasswordField("Confirm your password *", "confirm your password");
            TextField newDateOfBirthField = new TextField("Date of birth", "DD.MM.YYYY");
            ListBox<UserRole> newUserRoleListBox = new ListBox<>();
            newUserRoleListBox.setItems(UserRole.TEACHER, UserRole.STUDENT);
            RadioButtonGroup<String> newUserIsAdmin = new RadioButtonGroup<>();
            newUserIsAdmin.setLabel("Give this user Admin privileges?");
            newUserIsAdmin.setItems("Yes", "No");
            newUserIsAdmin.setValue("No");


        String warningText = "";

        Button addUserButton = new Button("Add user", event -> {
            User user = new User();
            User testUser = new User();

                user.setUsername(newUsernameField.getValue());
                user.setEmail(newEmailField.getValue());
                user.setName(newNameField.getValue());
                user.setSurname(newSurnameField.getValue());
                user.setPhoneNumber(newPhoneNumber.getValue());
                user.setPassword(HashingService.hashThis(newPasswordField.getValue()));
                user.setBirthDay(newDateOfBirthField.getValue());
                //   user.setRole(newUserRoleListBox.getValue());
                if(newUserIsAdmin.getValue().equals("Yes")){
                    user.setAdmin(true);
                }
                else
                if(newUserIsAdmin.getValue().equals("No")){
                    user.setAdmin(false);
                }


                boolean isok = true;

                if (newUsernameField.getValue() == null ||
                        // newEmailField.getValue()==null ||
                        newNameField.getValue() == null ||
                        newSurnameField.getValue() == null ||
                        // newPhoneNumber.getValue()==null ||
                        newPasswordField.getValue() == null ||
                        newConfirmField.getValue() == null ||
                        // newDateOfBirthField.getValue()==null ||
                        newUserRoleListBox.getDataProvider() == null ||

                        newUsernameField.getValue().equals("") ||
                        // newEmailField.getValue()=="" ||
                        newNameField.getValue().equals("") ||
                        newSurnameField.getValue().equals("") ||
                        // newPhoneNumber.getValue()=="" ||
                        newPasswordField.getValue().equals("") ||
                        newConfirmField.getValue().equals("")
                    // newDateOfBirthField.getValue()=="" ||
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
                    if (user.getEmail() != null && !user.getEmail().equals("")) {
                        Boolean emailTestUser;
                        emailTestUser = userRepository.existsByEmail(user.getEmail());
                        if (emailTestUser == true && user.getEmail() != null) {
                            //warningText.concat("This email has already been used!\n");
                            Notification.show("This email has already been used!\n");
                            isok = false;
                        }
                    }

                    if (user.getPhoneNumber() != null && !user.getPhoneNumber().equals("")) {
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

                    if (isok == true) {
                        testUser = userRepository.save(user);

                        if (testUser != null) {
                            Notification.show("User saved successfully");
                        } else {
                            Notification.show("Something went wrong. Please try again!");
                        }
                    }
                }
            });
            addUserButton.addClickShortcut(Key.ENTER);

            VerticalLayout addUserLayout = new VerticalLayout();

            addUserLayout.add(new H3("Add a new user"),
                    newUsernameField,
                    newEmailField,
                    newNameField,
                    newSurnameField,
                    newPhoneNumber,
                    newPasswordField,
                    newConfirmField,
                    newDateOfBirthField,
                    new H5("Select a role *"),
                    newUserRoleListBox,
                    newUserIsAdmin,
                    new H5("* - Mandatory field"),
                    addUserButton
            );

            add(addUserLayout);
        }
        else{
            add(new H5("You do not have admin role!"));
        }

    }

}

