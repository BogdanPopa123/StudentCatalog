package com.packagename.myapp.views;

import com.packagename.myapp.dao.DepartmentRepository;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.models.Department;
import com.packagename.myapp.models.Faculty;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;
import java.util.List;

@Route(value = "departments", layout = MainLayout.class)
@PageTitle("Department list")
@CssImport("./styles/shared-styles.css")
public class DepartmentView extends VerticalLayout {

    private final LoginService loginService;
    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;

    public DepartmentView(LoginService loginService, DepartmentRepository departmentRepository,
                          FacultyRepository facultyRepository) {
        this.loginService = loginService;
        this.departmentRepository = departmentRepository;
        this.facultyRepository = facultyRepository;
    }

    @PostConstruct
    private void init() {

        Grid<Department> grid = new Grid<>(Department.class);

        if (loginService.getAuthenticatedUser().isAdmin()) {
            com.vaadin.flow.component.textfield.TextField newDepartmentName = new com.vaadin.flow.component.textfield.TextField("New Department's Name");

            ComboBox<Faculty> facultyComboBox = new ComboBox<>();
            facultyComboBox.setLabel("Department's Faculty");
            List<Faculty> facultyList = facultyRepository.findAll();
            facultyComboBox.setItemLabelGenerator(Faculty::getName);
            facultyComboBox.setItems(facultyList);
            facultyComboBox.setValue(facultyList.get(0));

            Button addDepartment = new Button("Add department", event -> {
                boolean isok = true;
                if (newDepartmentName.getValue().equals(null)) {
                    Notification.show("Complete both fields");
                    isok = false;
                }
                if (departmentRepository.existsByName(newDepartmentName.getValue()) == true) {
                    Notification.show("This department name already exists.\nPlease try a new one!");
                    isok = false;
                }
                if (isok == true) {
                    Department testDepartment = null;
                    Department department = new Department();
                    department.setName(newDepartmentName.getValue());
                    department.setFaculty(facultyComboBox.getValue());
                    departmentRepository.save(department);
                    List<Department> departemnts = departmentRepository.findAll();
                    grid.setItems(departemnts);
                    Notification.show("Department saved successfully");
                }
                newDepartmentName.setValue("");
                facultyComboBox.setValue(facultyList.get(0));
            });

            addDepartment.setEnabled(false);
            newDepartmentName.setValueChangeMode(ValueChangeMode.EAGER);
            newDepartmentName.addValueChangeListener(e -> {
                if (newDepartmentName.getValue().trim().isEmpty()) {
                    addDepartment.setEnabled(false);
                } else {
                    addDepartment.setEnabled(true);
                }
            });

            VerticalLayout adminLayout = new VerticalLayout();
            adminLayout.add(newDepartmentName,
                    facultyComboBox,
                    addDepartment);
            add(grid, adminLayout);
        }

        grid.setItems(departmentRepository.findAll());
        grid.setColumns("id", "name", "faculty");

        add(grid);
    }
}