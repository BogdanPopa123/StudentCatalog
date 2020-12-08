package com.packagename.myapp.views.customComponents.manageButtons;

import com.packagename.myapp.models.BaseModel;
import com.packagename.myapp.services.NotificationService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class ManageButtons<T extends BaseModel> extends HorizontalLayout {
    private final static Logger logger = LogManager.getLogger(ManageButtons.class);

    private final NotificationService notificationService;
    private final Class<T> clazz;
    private final String tableName;

    private ModifyDialog<T> modifyDialog;

    private Runnable onSuccessfulModify;
    private Set<T> selectedItems = Collections.emptySet();

    public ManageButtons(Class<T> clazz) {
        this.notificationService = NotificationService.getService();
        this.clazz = clazz;

        this.tableName = this.getTableName();

        modifyDialog = new ModifyDialog<>(clazz);

        init();
    }

    private void init() {
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

    private void create(ClickEvent<Button> event) {
        modifyDialog.setNewBean();

        modifyDialog.addOnSuccessfulModifyListener(this::runOnSuccessfulModifyEvent);

        modifyDialog.open();
    }

    private void details(ClickEvent<Button> event) {
        if (selectedItems.isEmpty()) {
            notificationService.alert("Select a valid " + tableName + "!");
            return;
        }

        selectedItems.forEach(item -> new DetailsDialog(item).open());
    }

    private void modify(ClickEvent<Button> event) {
        if (selectedItems.isEmpty()) {
            notificationService.alert("Select a valid " + tableName + " to modify!");
            return;
        }

        selectedItems.forEach(item -> {
            modifyDialog.setBean(item);

            modifyDialog.addOnSuccessfulModifyListener(this::runOnSuccessfulModifyEvent);

            modifyDialog.open();
        });
    }

    private void delete(ClickEvent<Button> event) {
        if (selectedItems.isEmpty()) {
            notificationService.alert("Select a valid " + tableName + " to delete!");
            return;
        }

        selectedItems.forEach(item -> {
            DeleteDialog<BaseModel> deleteDialog = new DeleteDialog<>(item);

            deleteDialog.addOnConfirmEvent(this::runOnSuccessfulModifyEvent);

            deleteDialog.open();
        });
    }

    public void setSelectedItems(Set<T> selectedItems) {
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

    private Optional<T> createNewInstance() {
        try {
            return Optional.of(clazz.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        logger.error("Failed to create new instance of class: " + clazz.getSimpleName());
        return Optional.empty();
    }

    public String getTableName() {
        return createNewInstance().isPresent() ? createNewInstance().get().getEntityTableName() : "UNKNOWN";
    }

    public ModifyDialog<T> getModifyDialog() {
        return modifyDialog;
    }

    public void setModifyDialog(ModifyDialog<T> modifyDialog) {
        this.modifyDialog = modifyDialog;
    }
}
