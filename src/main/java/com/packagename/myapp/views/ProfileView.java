package com.packagename.myapp.views;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.*;
import com.packagename.myapp.models.*;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.customComponents.BaseModelTreeGrid;
import com.packagename.myapp.views.customComponents.manageButtons.ModifyDialog;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.compress.utils.Sets;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newHashSet;

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

    private Set<Profile> profiles;

    public ProfileView(ProfileRepository profileRepository, FacultyRepository facultyRepository,
                       LoginService loginService, DepartmentRepository departmentRepository,
                       DomainRepository domainRepository, SpecializationRepository specializationRepository,
                       StudentRepository studentRepository) {
        super(Profile.class);

        this.profileRepository = profileRepository;
        this.facultyRepository = facultyRepository;
        this.loginService = loginService;
        this.departmentRepository = departmentRepository;
        this.domainRepository = domainRepository;
        this.specializationRepository = specializationRepository;
        this.studentRepository = studentRepository;

        addClassName("profile-view");
    }

    @PostConstruct
    private void init() {


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
        grid.addColumn(BaseModel::getName).setHeader("Name").setKey("name").setWidth("20px");
        //profileGrid.addColumn(Profile::getAbbreviation).setHeader("Abbreviation").setKey("abbreviation").setWidth("20px");

        add(grid);

    }

    @Override
    protected void configureManageButtons() {

        ModifyDialog<Profile> modifyDialog = manageButtons.getModifyDialog();
        Binder<Profile> binder = modifyDialog.getBinder();

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



        binder.forField(status).bind(Profile::getStatus, Profile::setStatus);
        binder.forField(financingForm).bind(Profile::getFinancingForm, Profile::setFinancingForm);
        binder.forField(scholarshipType).bind(Profile::getScholarshipType, Profile::setScholarshipType);

        binder.forField(year).bind(Profile::getStudyYear, Profile::setStudyYear);

        modifyDialog.add(new VerticalLayout(
                new HorizontalLayout(facultySelect, departmentSelect),
                new HorizontalLayout(domainSelect, specializationSelect),
                new HorizontalLayout(financingForm, status),
                new HorizontalLayout(scholarshipType, year)
        ));
//  E NEVOIE DE BINDERE???


        manageButtons.addOnSuccessfulModifyListener(this::updateGrid);


    }

    @Override
    protected void updateGrid() {

        grid.setItems(Lists.newArrayList(profileRepository.findAll()));

    }
}
