package com.packagename.myapp.views;

import com.packagename.myapp.dao.GradeRepository;
import com.packagename.myapp.models.Grade;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.customComponents.manageButtons.ManageButtons;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = "catalog", layout = MainLayout.class)
@PageTitle("Catalog")
@CssImport("./styles/shared-styles.css")
public class CatalogView extends GradeView {

    private final LoginService loginService;
    private final GradeRepository gradeRepository;

    public CatalogView(LoginService loginService, GradeRepository gradeRepository) {
        super();
        this.loginService = loginService;
        this.gradeRepository = gradeRepository;
    }

    @Override
    protected void addHeader() {
        H5 header = new H5("Catalog");
        add(header);
    }

    @Override
    protected void addGrid() {
        super.addGrid();

        List<Grade> studentGrades = gradeRepository.findALlByProfile_Student_Id(loginService.getAuthenticatedUser().getId());
        grid.setItems(new ArrayList<>(studentGrades));
    }

    @Override
    protected void addManageButtons() {
    }

    @Override
    protected void configureManageButtons() {
    }
}
