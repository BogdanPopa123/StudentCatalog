package com.packagename.myapp.views.customComponents.manageButtons;

import com.packagename.myapp.models.BaseModel;
import com.packagename.myapp.services.NotificationService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Table;
import java.util.stream.StreamSupport;

public class CreateDialog<T extends BaseModel> extends Dialog {
    private static final Logger logger = LogManager.getLogger(CreateDialog.class);

    private final NotificationService notificationService;
    private final CrudRepository<T, Integer> repository;
    private final Class<T> clazz;
    private final TextField name = new TextField("Name");
    private String tableName;
    private Runnable onSuccessfulModify;
    private Binder<T> binder;


    public CreateDialog(Class<T> clazz,
                        CrudRepository<T, Integer> repository,
                        NotificationService notificationService) {
        this.notificationService = notificationService;
        this.clazz = clazz;
        this.repository = repository;

        setDialog();
        setBinder();
    }

    private void setDialog() {
        name.setWidth("300px");
        name.addKeyPressListener(Key.ENTER, this::save);

        Button save = new Button("Save", this::save);
        Button cancel = new Button("Cancel", this::cancel);

        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout createButtons = new HorizontalLayout(save, cancel);

        VerticalLayout subjectForm = new VerticalLayout(name, createButtons);

        add(subjectForm);
    }

    private void setBinder() {
        binder = new BeanValidationBinder<>(clazz);

        binder.setBean(createInstance());

        binder.forField(name)
                .asRequired("Enter name")
                .withValidator(s -> StreamSupport.stream(repository.findAll().spliterator(), false).noneMatch(item -> item.getName().equals(s)), "Name already taken")
                .bind(BaseModel::getName, BaseModel::setName);

    }

    private void save(ComponentEvent<? extends Component> event) {
        logger.debug("Submit new " + getTableName() + " data");

        if (!binder.isValid()) {
            logger.debug("Not valid " + getTableName() + " data");
            return;
        }

        T item = binder.getBean();

        logger.info("Save new " + getTableName());
        repository.save(item);

        runOnSuccessfulModifyEvent();

        notificationService.success("Saved subject!");

        close();
    }


    private void cancel(ClickEvent<Button> clickEvent) {
        this.close();
    }

    private T createInstance() {
        try {
            return this.clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.warn("Failed to instantiate object for class: " + this.clazz.getName(), e);
        }

        return null;
    }

    private String getTableName() {
        if (tableName == null) {
            tableName = clazz.getAnnotation(Table.class).name();
        }
        return tableName;
    }

    private void runOnSuccessfulModifyEvent() {
        if (this.onSuccessfulModify != null) {
            onSuccessfulModify.run();
        }
    }

    public void addOnSuccessfulModifyListener(Runnable onSuccessfulModify) {
        this.onSuccessfulModify = onSuccessfulModify;
    }

    private void configureComboBox(ComboBox<? extends BaseModel> comboBox, String placeholder, String width) {
        comboBox.setPlaceholder(placeholder);
        comboBox.setWidth(width);
        comboBox.setRequired(true);
        comboBox.setAllowCustomValue(false);
        comboBox.setPreventInvalidInput(true);
    }
}
