package com.packagename.myapp.views;

import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.User;
import com.packagename.myapp.views.layout.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Route(value = "students", layout = MainLayout.class)
@PageTitle("Studenti")
@CssImport("./styles/shared-styles.css")
public class StudentsView extends VerticalLayout{

    private Grid<User> grid = new Grid<>(User.class);

    UserRepository userRepository;

    public StudentsView(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void init(){

        createStudentGrid();

    }

    private void createStudentGrid() {
        add(new H3("Students"));

        //grid.setColumns("name", "surname", "username", "email", "phoneNumber", "birthDate");
        //grid.setDataProvider(new CrudRepository<User, Integer>(UserRepository));

        grid.addColumn(User::getName).setHeader("First Name");
        grid.addColumn(User::getSurname).setHeader("Last Name");
        grid.addColumn(User::getCnp).setHeader("CNP");
        grid.addColumn(User::getPhoneNumber).setHeader("Phone number");
        grid.addColumn(User::getEmail).setHeader("e-mail adress");

        grid.setHeightFull();

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addSelectionListener(selectionEvent -> {
            selectionEvent.getFirstSelectedItem().ifPresent(user -> {
                Notification.show(user.getName() + user.getSurname() + " is selected");
            });
        });

        final List<User> studentList = new ArrayList<>();
        userRepository.findAll().forEach(user -> studentList.add(user));

        final ListDataProvider<User> dataProvider = DataProvider.ofCollection(studentList);
        grid.setDataProvider(dataProvider);

    }

}