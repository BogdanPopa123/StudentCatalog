package com.packagename.myapp.views.customComponents.manageButtons;

import com.packagename.myapp.models.BaseModel;
import com.packagename.myapp.services.NotificationService;
import com.packagename.myapp.views.customComponents.HierarchicalCombobox;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

public class ModifyDialog<T extends BaseModel> extends Dialog {
    private static final Logger logger = LogManager.getLogger(ModifyDialog.class);

    private final NotificationService notificationService;
    private final CrudRepository<T, Integer> repository;
    private final Class<T> clazz;
    private final TextField name = new TextField("Name");
    private final T instance;
    protected Binder<T> binder;
    private Optional<HierarchicalCombobox> parent = Optional.empty();
    private String tableName;
    private Runnable onSuccessfulModify;
    private VerticalLayout formFields;


    public ModifyDialog(Class<T> clazz) {
        this.clazz = clazz;
        this.notificationService = NotificationService.getService();

        this.instance = createInstance();
        this.repository = getItemRepository(clazz);

        setDialog();
        setBinder();
    }

    private void setDialog() {
        name.setWidth("300px");
        name.addKeyPressListener(Key.ENTER, this::save);

        formFields = new VerticalLayout(name);

        if (instance.hasParent()) {
            List<HierarchicalCombobox> parentFields = instance.getParentTreeCombobox();
            this.parent = parentFields.stream().findFirst();

            parentFields.forEach(formFields::add);
        }

        VerticalLayout subjectForm = new VerticalLayout(formFields, getDialogButtons());

        add(subjectForm);
    }

    private HorizontalLayout getDialogButtons() {
        Button save = new Button("Save", this::save);
        Button cancel = new Button("Cancel", this::cancel);

        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        return new HorizontalLayout(save, cancel);
    }

    private void setBinder() {
        binder = new BeanValidationBinder<>(clazz);

        binder.setBean(createInstance());

        binder.forField(name)
                .asRequired("Enter name")
                .withValidator(instance::existsByName, "Name already taken")
                .bind(BaseModel::getName, BaseModel::setName);

        parent.ifPresent(baseModelComboBox -> binder.forField(baseModelComboBox)
                .asRequired("Select " + instance.getParentNewInstance().getEntityTableName())
                .withValidator(item -> {
                    Optional<BaseModel> parent = item.getRepository().findById(item.getId());
                    return parent.isPresent() && parent.get().getName().equals(item.getName());
                }, "Select a valid " + instance.getParentNewInstance().getEntityTableNameCapitalized())
                .bind(BaseModel::getParent, (t, parent) -> {
                    CrudRepository<? extends BaseModel, Integer> parentRepository = getParentRepository(clazz);
                    Optional<? extends BaseModel> parentFromRepository = parentRepository.findById(parent.getId());

                    parentFromRepository.ifPresent(t::setParent);
                }));

//        binder.bindInstanceFields(this);
    }

    private void save(ComponentEvent<? extends Component> event) {
        logger.debug("Submit new " + instance.getEntityTableNameCapitalized() + " data");

        BinderValidationStatus<T> validate = binder.validate();
        if (!binder.isValid()) {
            logger.debug("Not valid " + instance.getEntityTableNameCapitalized() + " data");
            return;
        }

        T item = binder.getBean();

        repository.save(item);

        String message = "Saved new " + instance.getEntityTableNameCapitalized();

        logger.info(message);
        notificationService.success(message);


        runOnSuccessfulModifyEvent();


        close();
    }


    private void cancel(ClickEvent<Button> clickEvent) {
        this.close();
    }

    private T createInstance() {
        try {
            return this.clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            logger.warn("Failed to instantiate object for class: " + this.clazz.getName(), e);
            return null;
        }
    }

    private void runOnSuccessfulModifyEvent() {
        if (this.onSuccessfulModify != null) {
            onSuccessfulModify.run();
        }
    }

    public void addOnSuccessfulModifyListener(Runnable onSuccessfulModify) {
        this.onSuccessfulModify = onSuccessfulModify;
    }

    public void setBean(T item) {
        binder.setBean(item);
    }

    private CrudRepository<T, Integer> getItemRepository(Class<T> clazz) {
        try {
            return instance.getRepository();
        } catch (NullPointerException e) {
            logger.error("Failed to get repository for: " + instance.getEntityTableNameCapitalized(), e);
        }

        return null;
    }

    private CrudRepository<? extends BaseModel, Integer> getParentRepository(Class<T> clazz) {
        return instance.getParentNewInstance().getRepository();
    }

    public void addFields(Component... field) {
        formFields.add(field);
    }


    public Binder<T> getBinder() {
        return binder;
    }

    public void setNewBean() {
        this.binder.setBean(createInstance());
    }

    public void removeAllFields() {
        formFields.removeAll();

        binder.removeBinding(name);
    }
}
