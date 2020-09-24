package com.packagename.myapp.views;


import com.google.common.collect.Lists;
import com.packagename.myapp.dao.DepartmentRepository;
import com.packagename.myapp.dao.DomainRepository;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.dao.SpecializationRepository;
import com.packagename.myapp.models.*;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.services.NotificationService;
import com.packagename.myapp.views.customComponents.BaseModelTreeGrid;
import com.packagename.myapp.views.layouts.MainLayout;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.repository.CrudRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Route(value = "specialization", layout = MainLayout.class)
@PageTitle("Specialization")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/specialization-style.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class SpecializationView extends VerticalLayoutAuthRestricted {
    private final Logger logger = LogManager.getLogger(SpecializationView.class);
    private final LoginService loginService;
    private final SpecializationRepository specializationRepository;
    private final DomainRepository domainRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final NotificationService notificationService;
    private final List<CrudRepository<? extends BaseModel, Integer>> repositories;
    private Dialog dialog;
    private TextField name = new TextField("Name");
    private ComboBox<Domain> domain = new ComboBox<>("Domain");
    private ComboBox<Faculty> faculty = new ComboBox<>("Faculty");
    private ComboBox<Department> department = new ComboBox<>("Department");
    private Binder<Specialization> binder = new BeanValidationBinder<>(Specialization.class);
    private BaseModelTreeGrid grid;

    public SpecializationView(LoginService loginService, SpecializationRepository specializationRepository, DomainRepository domainRepository, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, NotificationService notificationService) {
        super(loginService);
        this.loginService = loginService;
        this.specializationRepository = specializationRepository;
        this.domainRepository = domainRepository;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.notificationService = notificationService;

        repositories = Arrays.asList(facultyRepository, departmentRepository, domainRepository, specializationRepository);
    }

    @PostConstruct
    private void init() {
        addClassName("specialization-view");

        addHeader();
        addManageButtons();
        addTreeGrid();

        setCreateDialog();

        setBinder();
    }

    private void addHeader() {
        H1 header = new H1("Specialization");
        header.addClassName("specialization-header");
        add(header);
    }

    private void addTreeGrid() {
        grid = new BaseModelTreeGrid(repositories);

        grid.addHierarchyColumn(BaseModel::getName).setHeader("Specialiazations");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setHeight("800px");

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

        create.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        details.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        modify.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout manageButtons = new HorizontalLayout(create, details, modify, delete);
        add(manageButtons);
    }

    private void setCreateDialog() {
        faculty.addValueChangeListener(event -> {

            Set<Department> departments = event.getValue().getDepartments();
            department.setItems(departments);

            ArrayList<Domain> domains = departments.stream().map(Department::getDomains).collect(ArrayList::new, List::addAll, List::addAll);

            domain.setItems(domains);
        });
        faculty.setItems(Lists.newArrayList(facultyRepository.findAll()));
        faculty.setItemLabelGenerator(Faculty::getName);
        configureComboBox(faculty, "Faculty", "300px");

        department.addValueChangeListener(event -> domain.setItems(event.getValue() != null ? event.getValue().getDomains() : new ArrayList<>()));
        department.setItems(Lists.newArrayList(departmentRepository.findAll()));
        department.setItemLabelGenerator(Department::getName);
        configureComboBox(department, "Department", "300px");

        domain.setItems(Lists.newArrayList(domainRepository.findAll()));
        domain.setItemLabelGenerator(Domain::getName);
        configureComboBox(domain, "Domain", "300px");

        name.setWidth("300px");

        Button save = new Button("Save", this::save);
        Button cancel = new Button("Cancel", event -> {
            dialog.close();
            binder.setBean(new Specialization());
        });
        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        name.addKeyPressListener(Key.ENTER, event -> save.click());

        HorizontalLayout createButtons = new HorizontalLayout(save, cancel);

        VerticalLayout specializationForm = new VerticalLayout(faculty, department, domain, name, createButtons);

        dialog = new Dialog(specializationForm);
    }

    private void setBinder() {
        binder.setBean(new Specialization());

        binder.forField(domain)
                .asRequired("Select domain")
                .withValidator(d -> domainRepository.existsByName(d.getName()), "Not valid domain")
                .bind(Specialization::getDomain, Specialization::setDomain);

        binder.forField(name)
                .asRequired("Enter name")
                .withValidator(s -> !specializationRepository.existsByName(s), "Name already taken")
                .bind(Specialization::getName, Specialization::setName);

        binder.bindInstanceFields(this);
    }

    private void create(ClickEvent<Button> event) {
        dialog.open();
    }

    private void save(ClickEvent<Button> event) {
        logger.debug("Submit new specialization data");

        if (binder.isValid()) {
            Specialization specialization = binder.getBean();

            logger.info("Save new specialization");
            specializationRepository.save(specialization);

            dialog.close();
            grid.updateData();
        } else {
            logger.debug("Not valid specialization data");
        }
    }

    private void details(ClickEvent<Button> event) {

    }

    private void modify(ClickEvent<Button> event) {

    }

    private void delete(ClickEvent<Button> event) {
        Set<BaseModel> selectedItems = grid.getSelectedItems();
        selectedItems.forEach(item -> notificationService.info(item.toString()));
    }

    private void configureComboBox(ComboBox<? extends BaseModel> comboBox, String placeholder, String width) {
        comboBox.setPlaceholder(placeholder);
        comboBox.setWidth(width);
        comboBox.setRequired(true);
        comboBox.setAllowCustomValue(false);
        comboBox.setPreventInvalidInput(true);
    }


}
