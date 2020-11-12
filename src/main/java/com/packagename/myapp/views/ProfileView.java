package com.packagename.myapp.views;

import com.packagename.myapp.dao.*;
import com.packagename.myapp.models.*;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hibernate.stat.internal.DeprecatedNaturalIdCacheStatisticsImpl;
//import sun.rmi.runtime.Log;

import javax.annotation.PostConstruct;
import javax.xml.transform.dom.DOMResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(value = "profiles", layout = MainLayout.class)
@PageTitle("Student Profiles")
@CssImport("./styles/shared-styles.css")
public class ProfileView extends VerticalLayout {

    private final LoginService loginService;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final DomainRepository domainRepository;
    private final SpecializationRepository specializationRepository;
    private final StudentRepository studentRepository;

    public ProfileView(LoginService loginService, FacultyRepository facultyRepository,
                       DepartmentRepository departmentRepository, DomainRepository domainRepository,
                       SpecializationRepository specializationRepository, StudentRepository studentRepository){
        this.loginService = loginService;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.domainRepository = domainRepository;
        this.specializationRepository = specializationRepository;
        this.studentRepository = studentRepository;
    }

    @PostConstruct
    private void init(){



        if(loginService.getAuthenticatedUser().isAdmin()){
            Select<Faculty> facultySelect = new Select<>();
            facultySelect.setLabel("Faculty");
            Select<Department> departmentSelect = new Select<>();
            departmentSelect.setLabel("Department");
            Select<Domain> domainSelect = new Select<>();
            domainSelect.setLabel("Domain");
            Select<Specialization> specializationSelect = new Select<>();
            specializationSelect.setLabel("Specialization");
            Select<Integer> studyYear = new Select<>(1, 2, 3, 4);
            studyYear.setLabel("Study Year");
            Select<StudentClass> groupSelect = new Select<>();
            groupSelect.setLabel("Group");
            Select<Student> studentSelect = new Select<>();
            studentSelect.setLabel("Student");
            Select<Statut> statusSelect = new Select<>();
            statusSelect.setLabel("Status");
            Select<FormaFinantare> financingFormSelect = new Select<>();
            financingFormSelect.setLabel("Financing form");
            Select<TipBursa> scholarshipSelect = new Select<>();
            scholarshipSelect.setLabel("Scholarship type");
            Button addButton = new Button("Save");
            addButton.setEnabled(false);

            facultySelect.setItems(facultyRepository.findAll());
            facultySelect.addValueChangeListener(e->{
                departmentSelect.setValue(null);
                domainSelect.setValue(null);
                specializationSelect.setValue(null);
                addButton.setEnabled(false);

                departmentSelect.setItems(departmentRepository.findAllByFaculty_Id(facultySelect.getValue().getId()));
            });

            departmentSelect.addValueChangeListener(e2->{
                domainSelect.setValue(null);
                specializationSelect.setValue(null);
                addButton.setEnabled(false);

                domainSelect.setItems(domainRepository.findAllByDepartment(departmentSelect.getValue()));
            });

            domainSelect.addValueChangeListener(e3->{
                specializationSelect.setValue(null);
                addButton.setEnabled(false);

                specializationSelect.setItems(specializationRepository.findAllByDomain(domainSelect.getValue()));
            });

            specializationSelect.addValueChangeListener(e4->{
                addButton.setEnabled(true);
            });

            studyYear.setValue(1);
            //groupSelect.setValue(1);
            List<Student> studentList = studentRepository.findAll();
//            ArrayList<String> studentNames = new ArrayList<>();
//            for(Student student: studentList){
//                studentNames.add(student.getFullName());
//            }
            statusSelect.setItems(Statut.values());
            statusSelect.setValue(Statut.Inscris);
            financingFormSelect.setItems(FormaFinantare.values());
            financingFormSelect.setValue(FormaFinantare.Buget);
            scholarshipSelect.setItems(TipBursa.values());
            scholarshipSelect.setValue(TipBursa.Niciuna);




            addButton.addClickListener(e5->{
                if(!facultySelect.isEmpty() && !departmentSelect.isEmpty() && !domainSelect.isEmpty() &&
                !specializationSelect.isEmpty()){
                    Notification.show("Profile saved");
                }
            });







            add(facultySelect,
                    departmentSelect,
                    domainSelect,
                    specializationSelect,
                    new HorizontalLayout(studyYear, groupSelect, studentSelect),
                    new HorizontalLayout(statusSelect, financingFormSelect, scholarshipSelect));
        }
    }
}
