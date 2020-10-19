package com.packagename.myapp.views.customComponents.manageButtons;


import com.packagename.myapp.dao.SubjectRepository;
import com.packagename.myapp.models.BaseModel;
import com.packagename.myapp.models.Subject;
import com.packagename.myapp.services.NotificationService;
import com.packagename.myapp.views.SpecializationView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
import java.util.HashSet;
import java.util.Set;

@Component
@Scope(scopeName = "prototype")
public class SubjectViewManageButtons extends HorizontalLayout {

    private final Logger logger = LogManager.getLogger(SpecializationView.class);
    private final SubjectRepository subjectRepository;
    private final NotificationService notificationService;

    private Dialog dialog;
    private final TextField name = new TextField("Name");
    private Runnable onSuccessfulModify;

    private Set<Subject> selectedItems =new HashSet<>();

    private final Binder<Subject> binder = new BeanValidationBinder<>(Subject.class);

    public SubjectViewManageButtons(SubjectRepository subjectRepository, NotificationService notificationService) {
        this.subjectRepository = subjectRepository;
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
        name.setWidth("300px");

        Button save = new Button("Save", this::save);
        Button cancel = new Button("Cancel", event -> {
            dialog.close();
            binder.setBean(new Subject());
        });
        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        name.addKeyPressListener(Key.ENTER, event -> save.click());

        HorizontalLayout createButtons = new HorizontalLayout(save, cancel);

        VerticalLayout subjectForm = new VerticalLayout(name, createButtons);

        dialog = new Dialog(subjectForm);
    }

    private void setBinder() {
        binder.setBean(new Subject());

        binder.forField(name)
                .asRequired("Enter name")
                .withValidator(s -> !subjectRepository.existsByName(s), "Name already taken")
                .bind(Subject::getName, Subject::setName);
    }

    private void create(ClickEvent<Button> event) {
        dialog.open();
    }

    private void save(ClickEvent<Button> event) {
        logger.debug("Submit new subject data");

        if (!binder.isValid()) {
            logger.debug("Not valid subject data");
            return;
        }

        Subject subject = binder.getBean();

        logger.info("Save new subject");
        subjectRepository.save(subject);

        dialog.close();
        runOnSuccessfulModifyEvent();
        notificationService.success("Saved subject!");

        binder.setBean(new Subject());
    }

    private void details(ClickEvent<Button> event) {

        if (selectedItems.isEmpty()) {
            notificationService.alert("Select a valid subject!");
            return;
        }

        selectedItems.forEach(subject -> new DetailsDialog(subject).open());
    }

    private void modify(ClickEvent<Button> event) {
        if (selectedItems.isEmpty()) {
            notificationService.alert("Select a valid subject to modify!");
            return;
        }

        selectedItems.forEach(subject -> {
            binder.setBean(subject);

            dialog.open();
        });
    }

    private void delete(ClickEvent<Button> event) {
        if (selectedItems.isEmpty()) {
            notificationService.alert("Select a valid subject to delete!");
            return;
        }

        selectedItems.forEach(item -> {

            H5 header = new H5("Are you sure you want to delete subject:");
            H5 subject = new H5(item.toString() + " ?");

            Button confirm = new Button("Confirm");
            Button cancel = new Button("Cancel");

            confirm.addThemeVariants(ButtonVariant.LUMO_ERROR);
            cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            HorizontalLayout buttons = new HorizontalLayout(confirm, cancel);

            VerticalLayout dialogBody = new VerticalLayout(header, subject, buttons);
            dialogBody.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

            Dialog confirmDialog = new Dialog(dialogBody);

            confirm.addClickListener(click -> {
                subjectRepository.deleteById(item.getId());

                notificationService.success("Deleted subject: " + item.toString());

                logger.info("Deleted subject: " + item.toString());

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

    public Binder<Subject> getBinder() {
        return binder;
    }

    public void setSelectedItems(Set<Subject> selectedItems) {
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