package com.packagename.myapp.views.customComponents.manageButtons;

import com.google.common.base.Strings;
import com.packagename.myapp.Application;
import com.packagename.myapp.services.NotificationService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConfirmDialog extends Dialog {
    private final NotificationService notificationService = Application.context.getBean(NotificationService.class);

    private final String caption;
    private Runnable onConfirm;
    private String confirmMessage;

    public ConfirmDialog(String caption) {
        this.caption = caption;
        setDialog();
    }

    private void setDialog() {
        Html dialogMessage = new Html("<h3>" + caption + "</h3>");

        Button confirm = new Button("Confirm", this::confirm);
        Button cancel = new Button("Cancel", this::cancel);

        confirm.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttons = new HorizontalLayout(confirm, cancel);

        VerticalLayout dialogBody = new VerticalLayout(dialogMessage, buttons);

        dialogBody.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        add(dialogBody);

    }

    public void confirm(ClickEvent<Button> clickEvent) {
        runOnConfirm();
        showConfirmMessage();

        this.close();
    }

    public void cancel(ClickEvent<Button> clickEvent) {
        this.close();
    }

    private void showConfirmMessage() {
        notificationService.success(getConfirmMessage());
    }

    public void addOnConfirmEvent(Runnable onConfirm) {
        this.onConfirm = onConfirm;
    }

    private void runOnConfirm() {
        if (onConfirm != null) {
            onConfirm.run();
        }
    }

    private String getConfirmMessage() {
        return Strings.isNullOrEmpty(confirmMessage) ? "" : confirmMessage;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }
}
