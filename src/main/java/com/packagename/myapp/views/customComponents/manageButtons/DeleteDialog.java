package com.packagename.myapp.views.customComponents.manageButtons;

import com.packagename.myapp.models.BaseModel;
import com.packagename.myapp.services.NotificationService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.repository.CrudRepository;

public class DeleteDialog extends Dialog {

    private static final Logger logger = LogManager.getLogger(DeleteDialog.class);

    private final BaseModel item;
    private final NotificationService notificationService;
    private CrudRepository<? extends BaseModel, Integer> repository;
    private Runnable onConfirm;

    public DeleteDialog(BaseModel item,
                        CrudRepository<? extends BaseModel, Integer> repository,
                        NotificationService notificationService) {
        this.item = item;
        this.repository = repository;
        this.notificationService = notificationService;

        setDialog();
    }

    private void setDialog() {
        String message = "Are you sure you want to delete " + item.getEntityTableName() + " : \n" +
                         this.item.toShortString() + " ?";

        H5 dialogMessage = new H5(message);

        Button confirm = new Button("Confirm", this::confirm);
        Button cancel = new Button("Cancel", this::cancel);

        confirm.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttons = new HorizontalLayout(confirm, cancel);

        VerticalLayout dialogBody = new VerticalLayout(dialogMessage, buttons);

        dialogBody.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(dialogBody);
    }

    private void confirm(ClickEvent<Button> event) {
        repository.deleteById(item.getId());

        String message = "Deleted " + item.getEntityTableName() + " : " + item.toString();

        notificationService.success(message);
        logger.info(message);

        runOnConfirm();
        this.close();
    }

    private void cancel(ClickEvent<Button> event) {
        this.close();
    }

    public void addOnConfirmEvent(Runnable onConfirm) {
        this.onConfirm = onConfirm;
    }

    private void runOnConfirm() {
        if (onConfirm != null) {
            onConfirm.run();
        }
    }
}
