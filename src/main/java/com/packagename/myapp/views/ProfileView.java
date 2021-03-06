package com.packagename.myapp.views;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.*;
import com.packagename.myapp.models.*;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.customComponents.manageButtons.ModifyDialog;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Objects;
import java.util.Set;

//import sun.rmi.runtime.Log;

@Route(value = "profiles", layout = MainLayout.class)
@PageTitle("Student Profiles")
@CssImport("./styles/shared-styles.css")
public class ProfileView extends BaseModelView<Profile> {

    private final ProfileRepository profileRepository;
    private final LoginService loginService;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final DomainRepository domainRepository;
    private final SpecializationRepository specializationRepository;
    private final StudentRepository studentRepository;
    private final StudentClassRepository studentClassRepository;

    private Set<Profile> profiles;

    public ProfileView(ProfileRepository profileRepository, FacultyRepository facultyRepository,
                       LoginService loginService, DepartmentRepository departmentRepository,
                       DomainRepository domainRepository, SpecializationRepository specializationRepository,
                       StudentRepository studentRepository, StudentClassRepository studentClassRepository) {
        super(Profile.class);

        this.profileRepository = profileRepository;
        this.facultyRepository = facultyRepository;
        this.loginService = loginService;
        this.departmentRepository = departmentRepository;
        this.domainRepository = domainRepository;
        this.specializationRepository = specializationRepository;
        this.studentRepository = studentRepository;
        this.studentClassRepository = studentClassRepository;

        addClassName("profile-view");
    }


    @Override
    protected void addHeader() {

        H1 header = new H1("Profiles");
        header.addClassName("profile-header");
        add(header);
    }

    @Override
    protected void addGrid() {
        super.addGrid();
        grid.removeAllColumns();
//        facultyGrid.addColumn(Faculty::getId).setHeader("Id").setKey("id").setWidth("10px");
        grid.addColumn(o -> Objects.toString(((Profile) o).getStudent().getName(), "empty")).setKey("student").setHeader("Student");
        grid.addColumn(o -> Objects.toString(((Profile) o).getName(), "empty")).setKey("name").setHeader("Profile Name");
        grid.addColumn(o -> Objects.toString(((Profile) o).getStudentClass() != null ? ((Profile) o).getStudentClass().getName() : null, "empty")).setKey("group").setHeader("Group");
        grid.addColumn(o -> Objects.toString(((Profile) o).getStudyYear(), "empty")).setKey("year").setHeader("Study year");
        //profileGrid.addColumn(Profile::getAbbreviation).setHeader("Abbreviation").setKey("abbreviation").setWidth("20px");

        add(grid);

    }

    @Override
    protected void configureManageButtons() {


        ModifyDialog<Profile> modifyDialog = manageButtons.getModifyDialog();

        modifyDialog.removeAllFields();

        Binder<Profile> binder = modifyDialog.getBinder();

        TextField name = new TextField();
        name.setLabel("Profile name");

        Select<Faculty> facultySelect = new Select<>();
        facultySelect.setLabel("Select a faculty");

        Select<Department> departmentSelect = new Select<>();
        departmentSelect.setLabel("Select a department");
        departmentSelect.setItemLabelGenerator(Department::getName);

        Select<Domain> domainSelect = new Select<>();
        domainSelect.setLabel("Select a domain");
        domainSelect.setItemLabelGenerator(Domain::getName);

        Select<Specialization> specializationSelect = new Select<>();
        specializationSelect.setLabel("Select a specialization");
        specializationSelect.setItemLabelGenerator(Specialization::getName);

        facultySelect.setItems(facultyRepository.findAll());
        facultySelect.addValueChangeListener(e -> {
            departmentSelect.setValue(null);
            domainSelect.setValue(null);
            specializationSelect.setValue(null);

            departmentSelect.setItems(departmentRepository.findAllByFaculty_Id(facultySelect.getValue().getId()));
        });

        departmentSelect.addValueChangeListener(e2 -> {
            domainSelect.setValue(null);
            specializationSelect.setValue(null);
            domainSelect.setItems(domainRepository.findAllByDepartment(departmentSelect.getValue()));
        });

        domainSelect.addValueChangeListener(e3 -> {
            specializationSelect.setValue(null);

            specializationSelect.setItems(specializationRepository.findAllByDomain(domainSelect.getValue()));
        });

        Select<FormaFinantare> financingForm = new Select<>();
        financingForm.setLabel("Financing Form");
        financingForm.setItems(FormaFinantare.values());//FormaFinantare.values());
        financingForm.setValue(FormaFinantare.Buget);


        Select<Statut> status = new Select<>();
        status.setLabel("Status");
        status.setItems(Statut.values());
        status.setValue(Statut.Inscris);

        Select<TipBursa> scholarshipType = new Select<>();
        scholarshipType.setLabel("Scholarship type");
        scholarshipType.setItems(TipBursa.values());
        scholarshipType.setValue(TipBursa.Niciuna);

        Select<Integer> year = new Select<>();
        year.setLabel("Select study year");
        year.setItems(1, 2, 3, 4, 5, 6);
        year.setValue(1);

        Select<StudentClass> group = new Select<>();
        group.setLabel("Select a group");
        group.setItems(studentClassRepository.findAll());
        group.setItemLabelGenerator(StudentClass::getName);

        Select<Student> students = new Select<>();
        students.setLabel("Student name");
        students.setItemLabelGenerator(Student::getName);

        group.addValueChangeListener(e4 -> students.setItems(studentRepository.findAll()));

        binder.forField(name).withValidator(name1 -> !profileRepository.existsByName(name1), "This name is already taken")
                .bind(Profile::getName, Profile::setName);


        binder.forField(status).bind(Profile::getStatus, Profile::setStatus);
        binder.forField(financingForm).bind(Profile::getFinancingForm, Profile::setFinancingForm);
        binder.forField(scholarshipType).bind(Profile::getScholarshipType, Profile::setScholarshipType);
        binder.forField(students).bind(Profile::getStudent, Profile::setStudent);
        binder.forField(group).bind(Profile::getStudentClass, Profile::setStudentClass);

        binder.forField(year).bind(Profile::getStudyYear, Profile::setStudyYear);

        modifyDialog.addFields(new VerticalLayout(
                new HorizontalLayout(name),
                new HorizontalLayout(facultySelect, departmentSelect),
                new HorizontalLayout(domainSelect, specializationSelect),
                new HorizontalLayout(financingForm, status),
                new HorizontalLayout(scholarshipType, year),
                new HorizontalLayout(group, students)
        ));
//  E NEVOIE DE BINDERE???


        manageButtons.addOnSuccessfulModifyListener(this::updateGrid);


    }

    @Override
    protected void updateGrid() {

        grid.setItems(Lists.newArrayList(profileRepository.findAll()));

    }
}
