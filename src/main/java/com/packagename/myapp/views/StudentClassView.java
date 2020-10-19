package com.packagename.myapp.views;


import com.packagename.myapp.dao.*;
import com.packagename.myapp.models.*;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.List;

@Route(value = "StudentClasses", layout = MainLayout.class)
@PageTitle("Student classes")
@CssImport("./styles/shared-styles.css")
public class StudentClassView extends VerticalLayout {

    private final LoginService loginService;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final DomainRepository domainRepository;
    private final SpecializationRepository specializationRepository;
    private final StudentClassRepository studentClassRepository;

    public StudentClassView(LoginService loginService, FacultyRepository facultyRepository,
                            DepartmentRepository departmentRepository, DomainRepository domainRepository,
                            SpecializationRepository specializationRepository,
                            StudentClassRepository studentClassRepository) {
        this.loginService = loginService;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.domainRepository = domainRepository;
        this.specializationRepository = specializationRepository;
        this.studentClassRepository = studentClassRepository;
    }

        @PostConstruct
    private void init(){

        Grid<StudentClass> grid = new Grid<>(StudentClass.class);
        List<StudentClass> list = studentClassRepository.findAll();
        grid.setItems(list);

            if(loginService.getAuthenticatedUser().isAdmin()){
                TextField textField = new TextField("Group Name", "Insert a name");
                Select<Integer> studyYear = new Select<>();
                studyYear.setLabel("Select study year");
                studyYear.setItems(1, 2, 3, 4);
                studyYear.setValue(1);
                Select<Faculty> facultySelect = new Select<>();
                facultySelect.setLabel("Faculty");
                Select<Department> departmentSelect = new Select<>();
                departmentSelect.setLabel("Department");
                Select<Domain> domainSelect = new Select<>();
                domainSelect.setLabel("Domain");
                Select<Specialization> specializationSelect = new Select<>();
                specializationSelect.setLabel("Specialization");
                Button addButton = new Button("Save");
                addButton.setEnabled(false);

                textField.setValueChangeMode(ValueChangeMode.EAGER);
                textField.addValueChangeListener(e->{
                    if(textField.getValue().trim().isEmpty()){
                        addButton.setEnabled(false);
                    }
                    else{
                        addButton.setEnabled(true);
                    }
                });

                facultySelect.setItemLabelGenerator(Faculty::getName);
                departmentSelect.setItemLabelGenerator(Department::getName);
                domainSelect.setItemLabelGenerator(Domain::getName);
                specializationSelect.setItemLabelGenerator(Specialization::getName);


                facultySelect.setItems(facultyRepository.findAll());
                facultySelect.addValueChangeListener(e->{

                    departmentSelect.setValue(null);
                    domainSelect.setValue(null);
                    specializationSelect.setValue(null);

                    departmentSelect.setItems(departmentRepository.findAllByFaculty(facultySelect.getValue()));
                });

                departmentSelect.addValueChangeListener(e2->{

                    domainSelect.setValue(null);
                    specializationSelect.setValue(null);

                    domainSelect.setItems(domainRepository.findAllByDepartment(departmentSelect.getValue()));
                });

                domainSelect.addValueChangeListener(e3->{

                    specializationSelect.setValue(null);

                    specializationSelect.setItems(specializationRepository.findAllByDomain(domainSelect.getValue()));
                });

                addButton.addClickListener(e->{
                    if(!facultySelect.isEmpty() && !departmentSelect.isEmpty() && !domainSelect.isEmpty()
                    && !specializationSelect.isEmpty()){
                        StudentClass studentClass = new StudentClass();
                        studentClass.setName(textField.getValue());
                        studentClass.setStudyYear(studyYear.getValue());
                        studentClass.setSpecialization(specializationSelect.getValue());
                        studentClassRepository.save(studentClass);
                        list.add(studentClass);
                        grid.setItems(list);
                        Notification.show("Group saved successfully");
                    }
                    else{
                        Notification.show("All fields are mandatory");
                    }
                });

                VerticalLayout verticalLayout = new VerticalLayout(textField, studyYear, facultySelect,
                        departmentSelect, domainSelect, specializationSelect, addButton);
                add(verticalLayout, grid);
            }
            grid.setColumns("id", "name", "specialization");
            add(grid);
        }

}
