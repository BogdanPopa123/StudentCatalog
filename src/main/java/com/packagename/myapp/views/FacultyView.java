package com.packagename.myapp.views;

import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.models.Faculty;
import com.packagename.myapp.views.customComponents.manageButtons.ModifyDialog;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "faculty", layout = MainLayout.class)
@PageTitle("Faculty")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/faculty-view.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class FacultyView extends BaseModelView<Faculty> {

    private final FacultyRepository repository;
    public FacultyView(FacultyRepository repository) {
        super(Faculty.class);
        this.repository = repository;
    }

    protected void configureManageButtons() {
        ModifyDialog<Faculty> modifyDialog = manageButtons.getModifyDialog();
        Binder<Faculty> binder = modifyDialog.getBinder();

        TextField abbreviation = new TextField("Abbreviation");

        modifyDialog.addField(abbreviation);
        binder.forField(abbreviation)
                .asRequired("Enter abbreviation!")
                .withValidator(s -> !repository.existsByAbbreviation(s), "Abbreviation already taken!")
                .bind(Faculty::getAbbreviation, Faculty::setAbbreviation);

        super.configureManageButtons();
    }
}

