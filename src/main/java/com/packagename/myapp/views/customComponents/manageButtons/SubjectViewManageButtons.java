package com.packagename.myapp.views.customComponents.manageButtons;


import com.packagename.myapp.dao.SubjectRepository;
import com.packagename.myapp.models.Subject;
import com.packagename.myapp.services.NotificationService;
import com.packagename.myapp.views.SpecializationView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
@Scope(scopeName = "prototype")
public class SubjectViewManageButtons extends HorizontalLayout {

    private final Logger logger = LogManager.getLogger(SpecializationView.class);

    private final SubjectRepository subjectRepository;
    private final NotificationService notificationService;
    private Runnable onSuccessfulModify;
    private Set<Subject> selectedItems;

    public SubjectViewManageButtons(SubjectRepository subjectRepository, NotificationService notificationService) {
        this.subjectRepository = subjectRepository;
        this.notificationService = notificationService;
    }

    @PostConstruct
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

    private void create(ClickEvent<Button> clickEvent) {
        ModifyDialog<Subject> modifyDialog = new ModifyDialog<>(Subject.class);

        modifyDialog.addOnSuccessfulModifyListener(this::runOnSuccessfulModifyEvent);

        modifyDialog.open();
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
            ModifyDialog<Subject> modifyDialog = new ModifyDialog<>(Subject.class);

            modifyDialog.setBean(subject);

            modifyDialog.addOnSuccessfulModifyListener(this::runOnSuccessfulModifyEvent);

            modifyDialog.open();
        });
    }

    private void delete(ClickEvent<Button> event) {
        if (selectedItems.isEmpty()) {
            notificationService.alert("Select a valid subject to delete!");
            return;
        }

        selectedItems.forEach(item -> {
            DeleteDialog deleteDialog = new DeleteDialog(item);

            deleteDialog.addOnConfirmEvent(this::runOnSuccessfulModifyEvent);

            deleteDialog.open();
        });
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