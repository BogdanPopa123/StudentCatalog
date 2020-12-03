package com.packagename.myapp.views;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.StudentRepository;
import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.Student;
import com.packagename.myapp.models.User;
import com.packagename.myapp.models.UserRole;
import com.packagename.myapp.views.layouts.MainLayout;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Route(value = "students", layout = MainLayout.class)
@PageTitle("Students")
//@CssImport("./styles/shared-styles.css")
@CssImport("./styles/faculty-view.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class StudentsView extends VerticalLayoutAuthRestricted {

    private final Logger logger = LogManager.getLogger(StudentsView.class);

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    private List<User> students;
    private Grid<User> studentsGrid;

    public StudentsView(UserRepository userRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

    @PostConstruct
    private void init() {
        addClassName("students-view");
        setupHeader();
        setupGrid();
    }

    private void setupHeader() {
        H1 header = new H1("Students");
        header.addClassName("faculty-header");
        add(header);
    }

    private void setupGrid() {
        students = Lists.newArrayList(studentRepository.findAll());




        studentsGrid = new Grid<>();
        studentsGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        studentsGrid.setItems(students);

        studentsGrid.addColumn(User::getName).setHeader("Name").setKey("name").setWidth("15px");
        studentsGrid.addColumn(User::getSurname).setHeader("Surname").setKey("surname").setWidth("15px");
        studentsGrid.addColumn(User::getEmail).setHeader("e-mail").setKey("email").setWidth("20px");
        studentsGrid.addColumn(User::getPhoneNumber).setHeader("Phone number").setKey("phonenumber").setWidth("15px");

        add(studentsGrid);
    }
}

