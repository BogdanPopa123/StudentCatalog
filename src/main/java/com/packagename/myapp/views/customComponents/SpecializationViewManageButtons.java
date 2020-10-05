package com.packagename.myapp.views.customComponents;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.DepartmentRepository;
import com.packagename.myapp.dao.DomainRepository;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.dao.SpecializationRepository;
import com.packagename.myapp.models.*;
import com.packagename.myapp.services.NotificationService;
import com.packagename.myapp.views.SpecializationView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Scope(scopeName = "prototype")
public class SpecializationViewManageButtons extends HorizontalLayout {

    private final Logger logger = LogManager.getLogger(SpecializationView.class);
    private final SpecializationRepository specializationRepository;
    private final DomainRepository domainRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final NotificationService notificationService;

    private Dialog dialog;
    private TextField name = new TextField("Name");
    private ComboBox<Domain> domain = new ComboBox<>("Domain");
    private ComboBox<Faculty> faculty = new ComboBox<>("Faculty");
    private ComboBox<Department> department = new ComboBox<>("Department");
    private Runnable onSuccessfulModify;

    private Set<BaseModel> selectedItems = new HashSet<>();

    private Binder<Specialization> binder = new BeanValidationBinder<>(Specialization.class);

    public SpecializationViewManageButtons(SpecializationRepository specializationRepository,
                                           DomainRepository domainRepository,
                                           FacultyRepository facultyRepository,
                                           DepartmentRepository departmentRepository,
                                           NotificationService notificationService) {

        this.specializationRepository = specializationRepository;
        this.domainRepository = domainRepository;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.notificationService = notificationService;

    }

    @PostConstruct
    private void init() {
        setButtons();
        setCreateDialog();
        setBinder();
    }

    private void setButtons() {
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

        if (!binder.isValid()) {
            logger.debug("Not valid specialization data");
            return;
        }

        Specialization specialization = binder.getBean();

        logger.info("Save new specialization");
        specializationRepository.save(specialization);

        dialog.close();
        runOnSuccessfulModifyEvent();
        notificationService.success("Saved specialization!");

        binder.setBean(new Specialization());
    }

    private void details(ClickEvent<Button> event) {

        if (selectedItems.isEmpty()) {
            notificationService.alert("Select a valid specialization!");
            return;
        }

        selectedItems.forEach(item -> {
            Specialization specialization = (Specialization) item;

            int id = specialization.getId();
            String name = specialization.getName();
            Domain domain = specialization.getDomain();
            Department department = domain.getDepartment();
            Faculty faculty = department.getFaculty();

            Button close = new Button("Close");
            close.addThemeVariants(ButtonVariant.LUMO_ERROR);

            VerticalLayout specializationDetails = new VerticalLayout(
                    new H5("Id: " + id),
                    new H5("Name: " + name),
                    new H5("Domain: " + domain.getName()),
                    new H5("Department: " + department.getName()),
                    new H5("Faculty: " + faculty.getName())
            );

            VerticalLayout details = new VerticalLayout(
                    new H2("Specialization"),
                    new HtmlComponent("hr"),
                    specializationDetails,
                    close
            );
            
            details.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

            Dialog detailsDialog = new Dialog(details);

            close.addClickListener(click -> detailsDialog.close());

            detailsDialog.open();
        });
    }

    private void modify(ClickEvent<Button> event) {
        if (selectedItems.isEmpty()) {
            notificationService.alert("Select a valid specialization to modify!");
            return;
        }

        selectedItems.forEach(item -> {
            binder.setBean((Specialization) item);

            Domain domain = binder.getBean().getDomain();
            Department department = domain.getDepartment();
            Faculty faculty = department.getFaculty();

            this.faculty.setValue(faculty);
            this.department.setValue(department);
            this.domain.setValue(domain);

            dialog.open();
        });
    }

    private void delete(ClickEvent<Button> event) {
        if (selectedItems.isEmpty()) {
            notificationService.alert("Select a valid specialization to delete!");
            return;
        }

        selectedItems.forEach(item -> {

            H5 header = new H5("Are you sure you want to delete specialization:");
            H5 specialization = new H5(item.toShortString() + " ?");

            Button confirm = new Button("Confirm");
            Button cancel = new Button("Cancel");

            confirm.addThemeVariants(ButtonVariant.LUMO_ERROR);
            cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            HorizontalLayout buttons = new HorizontalLayout(confirm, cancel);

            VerticalLayout dialogBody = new VerticalLayout(header, specialization, buttons);
            dialogBody.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

            Dialog confirmDialog = new Dialog(dialogBody);

            confirm.addClickListener(click -> {
                specializationRepository.deleteById(item.getId());

                notificationService.success("Deleted specialization: " + item.toShortString());

                logger.info("Deleted specialization: " + item.toString());

                runOnSuccessfulModifyEvent();
                confirmDialog.close();
            });

            cancel.addClickListener(click -> confirmDialog.close());

            confirmDialog.open();
        });
    }

    private void configureComboBox(ComboBox<? extends BaseModel> comboBox, String placeholder, String width) {
        comboBox.setPlaceholder(placeholder);
        comboBox.setWidth(width);
        comboBox.setRequired(true);
        comboBox.setAllowCustomValue(false);
        comboBox.setPreventInvalidInput(true);
    }

    public Binder<Specialization> getBinder() {
        return binder;
    }

    public void setSelectedItems(Set<BaseModel> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public void addOnSuccessfulModifyListener(Runnable onSuccessfulModify) {
        this.onSuccessfulModify = onSuccessfulModify;
    }

    private void runOnSuccessfulModifyEvent() {
        if (this.onSuccessfulModify != null) {
            onSuccessfulModify.run();
        }
    }
}
