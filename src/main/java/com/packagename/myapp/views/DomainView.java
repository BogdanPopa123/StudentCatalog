package com.packagename.myapp.views;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.DepartmentRepository;
import com.packagename.myapp.dao.DomainRepository;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.models.BaseModel;
import com.packagename.myapp.models.Domain;
import com.packagename.myapp.models.Specialization;
import com.packagename.myapp.models.Subject;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.customComponents.BaseModelTreeGrid;
import com.packagename.myapp.views.customComponents.DomainViewManageButtons;
import com.packagename.myapp.views.layouts.MainLayout;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.repository.CrudRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "domain", layout = MainLayout.class)
@PageTitle("Domains")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/base-style-views.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class DomainView extends VerticalLayoutAuthRestricted {

    private final FacultyRepository facultyRepository;
    private final DomainRepository domainRepository;
    private final LoginService loginService;
    private final DomainViewManageButtons manageButtons;
    private final List<CrudRepository<? extends BaseModel, Integer>> repositories;

    private BaseModelTreeGrid grid = new BaseModelTreeGrid();

    public DomainView(LoginService loginService,
                      FacultyRepository facultyRepository,
                      DomainRepository domainRepository,
                      DepartmentRepository departmentRepository,
                      DomainViewManageButtons manageButtons) {
        super(loginService);
        this.loginService = loginService;
        this.facultyRepository = facultyRepository;
        this.domainRepository = domainRepository;
        this.manageButtons = manageButtons;

        repositories = Arrays.asList(facultyRepository, departmentRepository, domainRepository);
    }

    @PostConstruct
    private void init() {
        addClassName("base-class-views.css");
        setupHeader();
        addManageButtons();
        configureManageButtons();
        setupTreeGrid();

    }

    private void setupHeader(){
        H1 header = new H1("Domains");
        header.addClassName("faculty-header");
        add(header);
    }

    private void setupTreeGrid() {
        grid = new BaseModelTreeGrid(repositories);

        grid.addHierarchyColumn(BaseModel::getName).setHeader("Domains");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setHeight("800px");

        grid.addSelectionListener(event -> {
            Set<BaseModel> selectedItems = event.getAllSelectedItems().stream()
                    .filter(item -> item instanceof Domain)
                    .collect(Collectors.toSet());

            manageButtons.setSelectedItems(selectedItems);
        });

        add(grid);
        grid.expandAll();
    }

    private void addManageButtons() {
        if (loginService.getAuthenticatedUser().isNotAdmin()) {
            return;
        }

        add(manageButtons);
    }

    private void configureManageButtons() {
        manageButtons.getBinder().setBean(new Domain());
        manageButtons.addOnSuccessfulModifyListener(grid::updateDataAndExpandAll);
    }
}
