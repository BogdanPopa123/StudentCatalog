package com.packagename.myapp.views.customComponents.manageButtons;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConfirmDialog extends Dialog {

    private final String message;
    private Runnable onConfirm;

    public ConfirmDialog(String message) {
        this.message = message;
        setDialog();
    }

    private void setDialog() {
        Html dialogMessage = new Html("<h3>" + message + "</h3>");

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
        this.close();
    }

    public void cancel(ClickEvent<Button> clickEvent) {
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
