package com.packagename.myapp.views;

import com.packagename.myapp.Application;
import com.packagename.myapp.dao.StudentRepository;
import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.Student;
import com.packagename.myapp.models.User;
import com.packagename.myapp.services.HashingService;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.services.NotificationService;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "create-user", layout = MainLayout.class)
public class CreateNewDefaultUser extends VerticalLayout {

    private final UserRepository userRepository;
    private final NotificationService notification;
    private final LoginService loginService;
    private final StudentRepository studentRepository;

    public CreateNewDefaultUser(UserRepository userRepository,
                                NotificationService notification,
                                LoginService loginService,
                                StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.notification = notification;
        this.loginService = loginService;
        this.studentRepository = studentRepository;


//        newUser(userRepository, notification, loginService);

//        newStudent();

        debug();
    }

    private void debug() {


        studentRepository.findAll().forEach(student -> notification.success(student.toString()));
    }

    private void newStudent() {
        Student student = new Student();
        student.setUsername("student");
        student.setPassword(HashingService.hashThis("student"));

        Application.getService(StudentRepository.class).save(student);
    }

    private void newUser() {
        User user = new User();

        user.setUsername("root");
        user.setPassword(HashingService.hashThis("root"));
        user.setAdmin(true);

        userRepository.save(user);

        notification.success("Create new user");


        loginService.login("root", "root");
    }
}
