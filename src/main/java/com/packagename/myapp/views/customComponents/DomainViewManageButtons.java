package com.packagename.myapp.views.customComponents;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.DepartmentRepository;
import com.packagename.myapp.dao.DomainRepository;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.models.*;
import com.packagename.myapp.services.NotificationService;
import com.packagename.myapp.views.DomainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;


@Component
@Scope(scopeName = "prototype")
public class DomainViewManageButtons extends HorizontalLayout {

    private final Logger logger = LogManager.getLogger(DomainView.class);
    private final DomainRepository domainRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final NotificationService notificationService;

    private Dialog dialog;
    private TextField name = new TextField("Name");
    private TextField acronym = new TextField("Acronym");
    private ComboBox<Faculty> faculty = new ComboBox<>("Faculty");
    private ComboBox<Department> department = new ComboBox<>("Department");
    private Runnable onSuccessfulModify;

    private Set<BaseModel> selectedItems = new HashSet<>();

    private Binder<Domain> binder = new BeanValidationBinder<>(Domain.class);

    public DomainViewManageButtons(DomainRepository domainRepository,
                                   FacultyRepository facultyRepository,
                                   DepartmentRepository departmentRepository,
                                   NotificationService notificationService) {
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

        });

        faculty.setItems(Lists.newArrayList(facultyRepository.findAll()));
        faculty.setItemLabelGenerator(Faculty::getName);
        configureComboBox(faculty, "Faculty", "300px");

        department.setItems(Lists.newArrayList(departmentRepository.findAll()));
        department.setItemLabelGenerator(Department::getName);
        configureComboBox(department, "Department", "300px");

        name.setWidth("300px");

        Button save = new Button("Save", this::save);
        Button cancel = new Button("Cancel", event -> {
            dialog.close();
            binder.setBean(new Domain());
        });
        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        name.addKeyPressListener(Key.ENTER, event -> save.click());

        HorizontalLayout createButtons = new HorizontalLayout(save, cancel);

        VerticalLayout domainForm = new VerticalLayout(faculty, department, name, createButtons);

        dialog = new Dialog(domainForm);
    }

    private void setBinder() {
        binder.setBean(new Domain());

        binder.forField(department)
                .asRequired("Select domain")
                .withValidator(d -> departmentRepository.existsByName(d.getName()), "Not a valid department")
                .bind(Domain::getDepartment, Domain::setDepartment);

        binder.forField(name)
                .asRequired("Enter name")
                .withValidator(s -> !domainRepository.existsByName(s), "Name already taken")
                .bind(Domain::getName, Domain::setName);

        binder.bindInstanceFields(this);
    }

    private void create(ClickEvent<Button> event) {
        dialog.open();
    }

    private void save(ClickEvent<Button> event) {
        logger.debug("Submit new domain data");

        if (!binder.isValid()) {
            logger.debug("Not valid domain data");
            return;
        }

        Domain domain = binder.getBean();

        logger.info("Save new domain");
        domainRepository.save(domain);

        dialog.close();
        runOnSuccessfulModifyEvent();
        notificationService.success("Saved domain!");

        binder.setBean(new Domain());
    }

    private void details(ClickEvent<Button> event) {

        if (selectedItems.isEmpty()) {
            notificationService.alert("Select a valid domain!");
            return;
        }

        selectedItems.forEach(item -> {
            Domain domain = (Domain) item;

            int id = domain.getId();
            String name = domain.getName();
            Department department = domain.getDepartment();
            Faculty faculty = department.getFaculty();

            Button close = new Button("Close");
            close.addThemeVariants(ButtonVariant.LUMO_ERROR);

            VerticalLayout domainDetails = new VerticalLayout(
                    new H5("Id: " + id),
                    new H5("Name: " + name),
                    new H5("Department: " + department.getName()),
                    new H5("Faculty: " + faculty.getName())
            );

            VerticalLayout details = new VerticalLayout(
                    new H2("Domain"),
                    new HtmlComponent("hr"),
                    domainDetails,
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
            notificationService.alert("Select a valid domain to modify!");
            return;
        }

        selectedItems.forEach(item -> {
            binder.setBean((Domain) item);

            Department department = binder.getBean().getDepartment();
            Faculty faculty = department.getFaculty();

            this.faculty.setValue(faculty);
            this.department.setValue(department);

            dialog.open();
        });
    }

    private void delete(ClickEvent<Button> event) {
        if (selectedItems.isEmpty()) {
            notificationService.alert("Select a valid domain to delete!");
            return;
        }

        selectedItems.forEach(item -> {

            H5 header = new H5("Are you sure you want to delete the following domain:");
            H5 domain = new H5(item.toShortString() + " ?");

            Button confirm = new Button("Confirm");
            Button cancel = new Button("Cancel");

            confirm.addThemeVariants(ButtonVariant.LUMO_ERROR);
            cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            HorizontalLayout buttons = new HorizontalLayout(confirm, cancel);

            VerticalLayout dialogBody = new VerticalLayout(header, domain, buttons);
            dialogBody.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

            Dialog confirmDialog = new Dialog(dialogBody);

            confirm.addClickListener(click -> {
                domainRepository.deleteById(item.getId());

                notificationService.success("Deleted domain: " + item.toShortString());

                logger.info("Deleted domain: " + item.toString());

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

    public Binder<Domain> getBinder() {
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
