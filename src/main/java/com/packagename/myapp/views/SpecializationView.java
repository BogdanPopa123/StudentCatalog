package com.packagename.myapp.views;


import com.packagename.myapp.dao.DepartmentRepository;
import com.packagename.myapp.dao.DomainRepository;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.dao.SpecializationRepository;
import com.packagename.myapp.models.BaseModel;
import com.packagename.myapp.models.Specialization;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.customComponents.BaseModelTreeGrid;
import com.packagename.myapp.views.customComponents.manageButtons.ManageButtons;
import com.packagename.myapp.views.layouts.MainLayout;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.repository.CrudRepository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "specialization", layout = MainLayout.class)
@PageTitle("Specialization")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/specialization-style.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class SpecializationView extends VerticalLayoutAuthRestricted {
    private final LoginService loginService;
    private final List<CrudRepository<? extends BaseModel, Integer>> repositories;
    private final ManageButtons<Specialization> manageButtons = new ManageButtons<>(Specialization.class);
    private BaseModelTreeGrid grid = new BaseModelTreeGrid();

    public SpecializationView(LoginService loginService,
                              SpecializationRepository specializationRepository,
                              DomainRepository domainRepository,
                              FacultyRepository facultyRepository,
                              DepartmentRepository departmentRepository) {
        super(loginService);
        this.loginService = loginService;

        repositories = Arrays.asList(facultyRepository, departmentRepository, domainRepository, specializationRepository);
    }

    @PostConstruct
    private void init() {
        addClassName("specialization-view");

        addHeader();
        addManageButtons();
        addTreeGrid();

        configureManageButton();
    }

    private void addHeader() {
        H1 header = new H1("Specialization");
        header.addClassName("specialization-header");
        add(header);
    }

    private void addTreeGrid() {
        grid = new BaseModelTreeGrid(repositories);

        grid.addHierarchyColumn(BaseModel::getName).setHeader("Specializations");
//        grid.addColumn(baseModel -> baseModel.getClass().getSimpleName()).setHeader("Category");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setHeight("70vh");

        grid.addSelectionListener(event -> {
            Set<Specialization> selectedItems = event.getAllSelectedItems().stream()
                    .filter(item -> item instanceof Specialization)
                    .map(item -> (Specialization) item)
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

    private void configureManageButton() {
//        manageButtons.getBinder().setBean(new Specialization());
        manageButtons.addOnSuccessfulModifyListener(grid::updateDataAndExpandAll);
    }
}
