package com.packagename.myapp.views;

import com.packagename.myapp.models.Professor;
import com.packagename.myapp.views.customComponents.UserDialog;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Objects;

@Route(value = "professors", layout = MainLayout.class)
@PageTitle("Professor list")
@CssImport("./styles/shared-styles.css")
public class ProfessorView extends BaseModelView<Professor> {

    public ProfessorView() {
        super(Professor.class);
    }

    @Override
    protected void addGrid() {
        super.addGrid();

        grid.removeAllColumns();

        grid.addColumn(o -> Objects.toString(((Professor) o).getName(), "empty")).setKey("name").setHeader("Name");
        grid.addColumn(o -> Objects.toString(((Professor) o).getSurname(), "empty")).setKey("surname").setHeader("Surname");
        grid.addColumn(o -> Objects.toString(((Professor) o).getEmail(), "empty")).setKey("email").setHeader("Email");
        grid.addColumn(o -> Objects.toString(((Professor) o).getPhoneNumber(), "empty")).setKey("phone").setHeader("Phone Number");
    }

    @Override
    protected void configureManageButtons() {
        super.configureManageButtons();

        UserDialog.addUserFieldsToManageButtons(manageButtons.getModifyDialog());
    }
}
