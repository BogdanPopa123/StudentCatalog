package com.packagename.myapp.views;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.packagename.myapp.dao.DepartmentRepository;
import com.packagename.myapp.dao.DomainRepository;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.dao.SpecializationRepository;
import com.packagename.myapp.models.*;
import com.packagename.myapp.models.Domain;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layouts.MainLayout;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
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
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

    private Dialog dialog;
    private TextField name = new TextField("Name");
    private ComboBox<Domain> domain = new ComboBox<>("Domain");
    private ComboBox<Faculty> faculty = new ComboBox<>("Faculty");
    private ComboBox<Department> department = new ComboBox<>("Department");
    private Binder<Specialization> binder = new BeanValidationBinder<>(Specialization.class);
    private Grid<Specialization> grid = new Grid<>();

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
        addTreeGrid();
        addManageButtons();

        setCreateDialog();

        setBinder();
    }

    private void addHeader() {
        H1 header = new H1("Specialization");
        header.addClassName("specialization-header");
        add(header);
    }

    private void addGrid() {
        ArrayList<Specialization> specializations = Lists.newArrayList(specializationRepository.findAll());

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setItems(specializations);

        grid.addColumn(Specialization::getId).setHeader("Id").setKey("id");
        grid.addColumn(Specialization::getName).setHeader("Name").setKey("name");
        grid.addColumn(specialization -> specialization.getDomain().getName()).setHeader("Domain").setKey("domain");
        grid.addColumn(specialization -> specialization.getDomain().getDepartment().getName()).setHeader("Department").setKey("department");
        grid.addColumn(specialization -> specialization.getDomain().getDepartment().getFaculty().getName()).setHeader("Faculty").setKey("faculty");

        add(grid);
    }

    private void addTreeGrid() {
        TreeGrid<String> treeGrid = new TreeGrid<>();

        List<BaseModel> faculties = Lists.newArrayList(facultyRepository.findAll());

        List<BaseModel> departments = faculties.stream().map(BaseModel::getChildren).collect(ArrayList::new, List::addAll, List::addAll);

        List<BaseModel> domains = departments.stream().map(BaseModel::getChildren).collect(ArrayList::new, List::addAll, List::addAll);

        List<BaseModel> specializations = domains.stream().map(BaseModel::getChildren).collect(ArrayList::new, List::addAll, List::addAll);

//        treeGrid.setItems(faculties, BaseModel::getChildren);

        TreeData<String> treeData = new TreeData<>();
//
        faculties.forEach(baseModel -> treeData.addItem(null, baseModel.getName()));
        departments.forEach(baseModel -> treeData.addItem(baseModel.getParent().getName(), baseModel.getName()));
        domains.forEach(baseModel -> treeData.addItem(baseModel.getParent().getName(), baseModel.getName()));
        specializations.forEach(baseModel -> treeData.addItem(baseModel.getParent().getName(), baseModel.getName()));

//        faculties.forEach(baseModel -> treeGrid.getTreeData().addItem(baseModel.getParent(), baseModel));

        TreeDataProvider<String> treeDataProvider = new TreeDataProvider<>(treeData);
        treeGrid.setDataProvider(treeDataProvider);
//        departments.forEach(baseModel -> treeGrid.getTreeData().addItem(baseModel.getParent(), baseModel));

//        rootModels.forEach(baseModel -> grid.);

//        rootModels.forEach(baseModel -> treeGrid.getTreeData().addItem(baseModel.getParent(), baseModel));
//
        treeGrid.addHierarchyColumn(s -> s)
                .setHeader("Value");


        add(treeGrid);
//        TreeData<BaseModel> treeData = new TreeData<>();
//        treeData.addItems(facultyRepository.);
//
//        facultyRepository.findAll().forEach();
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
        faculty.setPlaceholder("Faculty");
        faculty.setItemLabelGenerator(Faculty::getName);
        faculty.setWidth("300px");
        faculty.setRequired(true);
        faculty.setAllowCustomValue(false);
        faculty.setPreventInvalidInput(true);

        department.addValueChangeListener(event -> domain.setItems(event.getValue() != null ? event.getValue().getDomains() : new ArrayList<>()));
        department.setItems(Lists.newArrayList(departmentRepository.findAll()));
        department.setPlaceholder("Department");
        department.setItemLabelGenerator(Department::getName);
        department.setWidth("300px");
        department.setRequired(true);
        department.setAllowCustomValue(false);
        department.setPreventInvalidInput(true);

        domain.setItems(Lists.newArrayList(domainRepository.findAll()));
        domain.setPlaceholder("Domain");
        domain.setItemLabelGenerator(Domain::getName);
        domain.setWidth("300px");
        domain.setRequired(true);
        domain.setAllowCustomValue(false);
        domain.setPreventInvalidInput(true);

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
            updateGrid();
        } else {
            logger.debug("Not valid specialization data");
        }
    }

    private void details(ClickEvent<Button> event) {

    }

    private void modify(ClickEvent<Button> event) {

    }

    private void delete(ClickEvent<Button> event) {
    }

    private void updateGrid() {
        ArrayList<Specialization> specializations = Lists.newArrayList(specializationRepository.findAll());
        grid.setItems(specializations);
    }

}
