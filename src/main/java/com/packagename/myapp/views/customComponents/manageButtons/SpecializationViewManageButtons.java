package com.packagename.myapp.views.customComponents.manageButtons;

import com.packagename.myapp.dao.DepartmentRepository;
import com.packagename.myapp.dao.DomainRepository;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.dao.SpecializationRepository;
import com.packagename.myapp.models.*;
import com.packagename.myapp.services.NotificationService;
import com.packagename.myapp.views.SpecializationView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    private final Binder<Specialization> binder = new BeanValidationBinder<>(Specialization.class);
//    private Dialog dialog;
    private TextField name = new TextField("Name");
    private ComboBox<Domain> domain = new ComboBox<>("Domain");
    private ComboBox<Faculty> faculty = new ComboBox<>("Faculty");
    private ComboBox<Department> department = new ComboBox<>("Department");
    private Runnable onSuccessfulModify;
    private Set<BaseModel> selectedItems;

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
//        setCreateDialog();
//        setBinder();
    }

    private void setButtons() {
        Button create = new Button("Create", this::create);
        Button details = new Button("Details", this::details);
//        Button modify = new Button("Modify", this::modify);
        Button delete = new Button("Delete", this::delete);

        create.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        details.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//        modify.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout manageButtons = new HorizontalLayout(create, details, delete);
//        HorizontalLayout manageButtons = new HorizontalLayout(create, details, modify, delete);
        add(manageButtons);
    }

//    private void setCreateDialog() {
//        faculty.addValueChangeListener(event -> {
//
//            Set<Department> departments = event.getValue().getDepartments();
//            department.setItems(departments);
//
//            ArrayList<Domain> domains = departments.stream().map(Department::getDomains).collect(ArrayList::new, List::addAll, List::addAll);
//
//            domain.setItems(domains);
//        });
//
//        faculty.setItems(Lists.newArrayList(facultyRepository.findAll()));
//        faculty.setItemLabelGenerator(Faculty::getName);
//        configureComboBox(faculty, "Faculty", "300px");
//
//        department.addValueChangeListener(event -> domain.setItems(event.getValue() != null ? event.getValue().getDomains() : new ArrayList<>()));
//        department.setItems(Lists.newArrayList(departmentRepository.findAll()));
//        department.setItemLabelGenerator(Department::getName);
//        configureComboBox(department, "Department", "300px");
//
//        domain.setItems(Lists.newArrayList(domainRepository.findAll()));
//        domain.setItemLabelGenerator(Domain::getName);
//        configureComboBox(domain, "Domain", "300px");
//
//        name.setWidth("300px");
//
//        Button save = new Button("Save", this::save);
//        Button cancel = new Button("Cancel", event -> {
//            dialog.close();
//            binder.setBean(new Specialization());
//        });
//        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
//        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
//
//        name.addKeyPressListener(Key.ENTER, event -> save.click());
//
//        HorizontalLayout createButtons = new HorizontalLayout(save, cancel);
//
//        VerticalLayout specializationForm = new VerticalLayout(faculty, department, domain, name, createButtons);
//
//        List<com.vaadin.flow.component.Component> fields = new Specialization().getPropertiesField();
//        fields.forEach(specializationForm::add);
//
//        dialog = new Dialog(specializationForm);
//    }
//
//    private void setBinder() {
//        binder.setBean(new Specialization());
//
//        binder.forField(domain)
//                .asRequired("Select domain")
//                .withValidator(d -> domainRepository.existsByName(d.getName()), "Not valid domain")
//                .bind(Specialization::getDomain, Specialization::setDomain);
//
//        binder.forField(name)
//                .asRequired("Enter name")
//                .withValidator(s -> !specializationRepository.existsByName(s), "Name already taken")
//                .bind(Specialization::getName, Specialization::setName);
//
//        binder.bindInstanceFields(this);
//    }

    private void create(ClickEvent<Button> event) {
        ModifyDialog<Specialization> modifyDialog = new ModifyDialog<>(Specialization.class);

        modifyDialog.addOnSuccessfulModifyListener(this::runOnSuccessfulModifyEvent);

        modifyDialog.open();
    }

//    private void save(ClickEvent<Button> event) {
//        logger.debug("Submit new specialization data");
//
//        if (!binder.isValid()) {
//            logger.debug("Not valid specialization data");
//            return;
//        }
//
//        Specialization specialization = binder.getBean();
//
//        logger.info("Save new specialization");
//        specializationRepository.save(specialization);
//
//        dialog.close();
//        runOnSuccessfulModifyEvent();
//        notificationService.success("Saved specialization!");
//
//        binder.setBean(new Specialization());
//    }

    private void details(ClickEvent<Button> event) {
        if (selectedItems.isEmpty()) {
            notificationService.alert("Select a valid specialization!");
            return;
        }

        selectedItems.forEach(item -> new DetailsDialog(item).open());
    }

//    private void modify(ClickEvent<Button> event) {
//        if (selectedItems.isEmpty()) {
//            notificationService.alert("Select a valid specialization to modify!");
//            return;
//        }
//
//        selectedItems.forEach(item -> {
//            binder.setBean((Specialization) item);
//
//            Domain domain = binder.getBean().getDomain();
//            Department department = domain.getDepartment();
//            Faculty faculty = department.getFaculty();
//
//            this.faculty.setValue(faculty);
//            this.department.setValue(department);
//            this.domain.setValue(domain);
//
//            dialog.open();
//        });
//    }

    private void delete(ClickEvent<Button> event) {
        if (selectedItems.isEmpty()) {
            notificationService.alert("Select a valid specialization to delete!");
            return;
        }

        selectedItems.forEach(item -> {
            DeleteDialog<BaseModel> deleteDialog = new DeleteDialog<>(item);

            deleteDialog.addOnConfirmEvent(this::runOnSuccessfulModifyEvent);

            deleteDialog.open();
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
