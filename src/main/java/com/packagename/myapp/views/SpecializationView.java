package com.packagename.myapp.views;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.DepartmentRepository;
import com.packagename.myapp.dao.DomainRepository;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.dao.SpecializationRepository;
import com.packagename.myapp.models.Department;
import com.packagename.myapp.models.Domain;
import com.packagename.myapp.models.Faculty;
import com.packagename.myapp.models.Specialization;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layouts.MainLayout;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Route(value = "specialization", layout = MainLayout.class)
@PageTitle("Specialization")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/specialization-style.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class SpecializationView extends VerticalLayoutAuthRestricted {
    private final LoginService loginService;
    private final SpecializationRepository specializationRepository;
    private final DomainRepository domainRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;

    private Dialog dialog;
    private TextField name;
    private ComboBox<Domain> domain;
    private ComboBox<Faculty> faculty;
    private ComboBox<Department> department;

    public SpecializationView(LoginService loginService, SpecializationRepository specializationRepository, DomainRepository domainRepository, FacultyRepository facultyRepository, DepartmentRepository departmentRepository) {
        super(loginService);
        this.loginService = loginService;
        this.specializationRepository = specializationRepository;
        this.domainRepository = domainRepository;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
    }

    @PostConstruct
    private void init() {
        addClassName("specialization-view");

        addHeader();
        addGrid();
        addManageButtons();

        setCreateDialog();
    }

    private void addHeader() {
        H1 header = new H1("Specialization");
        header.addClassName("specialization-header");
        add(header);
    }

    private void addGrid() {
        ArrayList<Specialization> specializations = Lists.newArrayList(specializationRepository.findAll());

        Grid<Specialization> grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setItems(specializations);

        grid.addColumn(Specialization::getId).setHeader("Id").setKey("id");
        grid.addColumn(Specialization::getName).setHeader("Name").setKey("name");
        grid.addColumn(specialization -> specialization.getDomain().getName()).setHeader("Domain").setKey("domain");

        add(grid);
    }

    private void addManageButtons() {
        if (loginService.getAuthenticatedUser().isNotAdmin()) {
            return;
        }

        Button create = new Button("Create", this::create);
        Button details = new Button("Details", this::details);
        Button modify = new Button("Modify", this::modify);
        Button delete = new Button("Delete", this::delete);

        create.addThemeName(ButtonVariant.LUMO_SUCCESS.getVariantName());
        details.addThemeName(ButtonVariant.LUMO_TERTIARY.getVariantName());
        modify.addThemeName(ButtonVariant.LUMO_TERTIARY.getVariantName());
        delete.addThemeName(ButtonVariant.LUMO_ERROR.getVariantName());

        HorizontalLayout manageButtons = new HorizontalLayout(create, details, modify, delete);
        add(manageButtons);
    }

    private void setCreateDialog() {
        faculty = new ComboBox<>("Faculty");
        faculty.addValueChangeListener(event -> department.setItems(event.getValue().getDepartments()));
        faculty.setItems(Lists.newArrayList(facultyRepository.findAll()));
        faculty.setPlaceholder("Faculty");
        faculty.setItemLabelGenerator(Faculty::getName);
        faculty.setWidth("300px");

        department = new ComboBox<>("Department");
        department.addValueChangeListener(event -> domain.setItems(event.getValue() != null ? event.getValue().getDomains() : new ArrayList<>()));
        department.setItems(Lists.newArrayList(departmentRepository.findAll()));
        department.setPlaceholder("Department");
        department.setItemLabelGenerator(Department::getName);
        department.setWidth("300px");

        domain = new ComboBox<>("Domain");
        domain.setItems(Lists.newArrayList(domainRepository.findAll()));
        domain.setPlaceholder("Domain");
        domain.setItemLabelGenerator(Domain::getName);
        domain.setWidth("300px");

        name = new TextField("Name");
        name.setWidth("300px");


        Button save = new Button("Save", this::save);
        Button cancel = new Button("Cancel", event -> dialog.close());
        save.addThemeName(ButtonVariant.LUMO_SUCCESS.getVariantName());
        cancel.addThemeName(ButtonVariant.LUMO_ERROR.getVariantName());

        HorizontalLayout createButtons = new HorizontalLayout(save, cancel);

        VerticalLayout specializationForm = new VerticalLayout(faculty, department, domain, name, createButtons);
        dialog = new Dialog(specializationForm);
    }

    private void create(ClickEvent<Button> event) {
        dialog.open();
    }

    private void save(ClickEvent<Button> event) {

    }

    private void details(ClickEvent<Button> event) {

    }

    private void modify(ClickEvent<Button> event) {

    }

    private void delete(ClickEvent<Button> event) {
    }

}
