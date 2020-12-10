package com.packagename.myapp.views;

import com.packagename.myapp.models.Student;
import com.packagename.myapp.models.User;
import com.packagename.myapp.views.customComponents.UserDialog;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Objects;

@Route(value = "students", layout = MainLayout.class)
@PageTitle("Students")
//@CssImport("./styles/shared-styles.css")
@CssImport("./styles/faculty-view.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class StudentsView extends BaseModelView<Student> {
    public StudentsView() {
        super(Student.class);
    }

    @Override
    protected void addGrid() {
        super.addGrid();

        grid.removeAllColumns();

        grid.addColumn(o -> Objects.toString(((User) o).getName(), "empty")).setKey("name").setHeader("Name");
        grid.addColumn(o -> Objects.toString(((User) o).getSurname(), "empty")).setKey("surname").setHeader("Surname");
        grid.addColumn(o -> Objects.toString(((User) o).getEmail(), "empty")).setKey("email").setHeader("Email");
        grid.addColumn(o -> Objects.toString(((User) o).getPhoneNumber(), "empty")).setKey("phone").setHeader("Phone Number");
    }

    @Override
    protected void configureManageButtons() {
        super.configureManageButtons();

        UserDialog.addUserFieldsToManageButtons(manageButtons.getModifyDialog());
    }
}

