package com.packagename.myapp.views;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.DepartmentRepository;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.models.Department;
import com.packagename.myapp.models.Faculty;
import com.packagename.myapp.models.UniversityModel;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layout.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
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
        TreeGrid<UniversityModel> grid = new TreeGrid<>();


        if (loginService.getAuthenticatedUser().isAdmin()) {
            com.vaadin.flow.component.textfield.TextField newDepartmentName = new com.vaadin.flow.component.textfield.TextField("New Department's Name");
            com.vaadin.flow.component.textfield.TextField facultyField = new com.vaadin.flow.component.textfield.TextField("id of the faculty of the new department");
            Button addDepartment = new Button("Add department", event -> {
                boolean isok = true;
                if (newDepartmentName.getValue().equals(null) ||
                        !facultyField.getValue().matches("\\d+") ||
                        facultyField.getValue().equals(null)) {
                    Notification.show("Complete both fields");
                    isok = false;
                }
                if (facultyRepository.existsById(Integer.parseInt(facultyField.getValue())) == false) {
                    Notification.show("The faculty with this id does not exist");
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
                    department.setFaculty(facultyRepository.findById(Integer.parseInt(facultyField.getValue())));
                    testDepartment = departmentRepository.save(department);
                    if (testDepartment != null) {
                        Notification.show("Department saved successfully");
                    } else {
                        Notification.show("Something went wrong! Please try again.");
                    }
                }
            });

            VerticalLayout adminLayout = new VerticalLayout();
            adminLayout.add(newDepartmentName,
                    facultyField,
                    addDepartment);
            add(grid, adminLayout);
        }

        grid.setItems(new ArrayList<UniversityModel>(facultyRepository.findAll()), universityModel -> {
            if (universityModel instanceof Faculty)
                return new ArrayList<>(departmentRepository.findAllByFaculty_Id(universityModel.getId()));
            else
                return new ArrayList<>();
            //   .stream()
            //    .forEach();
        });
        grid.addHierarchyColumn(UniversityModel::getName).setHeader("Faculty and departments");
        //grid.addColumn(UniversityModel::getName).setHeader("Department");


        add(grid);


    }


}
