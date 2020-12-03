package com.packagename.myapp.views;

import com.packagename.myapp.dao.DepartmentRepository;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.models.BaseModel;
import com.packagename.myapp.models.Department;
import com.packagename.myapp.views.customComponents.BaseModelTreeGrid;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.repository.CrudRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "departments", layout = MainLayout.class)
@PageTitle("Department list")
@CssImport("./styles/shared-styles.css")
public class DepartmentView extends BaseModelView<Department> {

    private final DepartmentRepository departmentRepository;
    private BaseModelTreeGrid grid;
    private final List<CrudRepository<? extends BaseModel, Integer>> repositories;

    public DepartmentView(DepartmentRepository departmentRepository, FacultyRepository facultyRepository) {
        super(Department.class);
        this.departmentRepository = departmentRepository;
        repositories = Arrays.asList(facultyRepository, departmentRepository);
    }

    @Override
    protected void addHeader() {
        H1 header = new H1("Departments");
        add(header);
    }

    @Override
    protected void addGrid() {
        grid = new BaseModelTreeGrid(repositories);
        grid.addHierarchyColumn(BaseModel::getName).setHeader("Departments");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setHeight("70vh");

        grid.addSelectionListener(event -> {
            Set<Department> departments = event.getAllSelectedItems().stream()
                    .filter(item -> item instanceof Department)
                    .map(item -> (Department) item)
                    .collect(Collectors.toSet());
            manageButtons.setSelectedItems(departments);
        });
        add(grid);
        grid.expandAll();
    }

    @Override
    protected void configureManageButtons() {
        this.manageButtons.addOnSuccessfulModifyListener(this::updateGrid);
    }

    @Override
    protected void updateGrid() {
        grid.updateDataAndExpandAll();
    }


}