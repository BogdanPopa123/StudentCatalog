package com.packagename.myapp.views;

import com.packagename.myapp.models.BaseModel;
import com.packagename.myapp.models.Specialization;
import com.packagename.myapp.views.customComponents.BaseModelTreeGrid;
import com.packagename.myapp.views.customComponents.manageButtons.ManageButtons;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.treegrid.TreeGrid;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseModelView<T extends BaseModel> extends VerticalLayoutAuthRestricted {

    protected final ManageButtons<T> manageButtons;
    private final Class<T> clazz;
    protected TreeGrid<BaseModel> grid;

    public BaseModelView(Class<T> clazz) {
        this.clazz = clazz;
        manageButtons = new ManageButtons<>(this.clazz);
    }

    @PostConstruct
    private void init() {
        addHeader();
        addManageButtons();
        addGrid();
        configureManageButtons();
    }

    protected void addHeader() {
        H1 header = new H1(Objects.requireNonNull(createNewInstanceOfT()).getEntityTableNameCapitalized());
        add(header);
    }


    protected void addManageButtons() {
        if (loginService.getAuthenticatedUser().isNotAdmin()) {
            return;
        }

        add(manageButtons);
    }


    protected void addGrid() {
        grid = new BaseModelTreeGrid(clazz);
//        grid = new BaseModelTreeGrid(repositories);

        grid.addHierarchyColumn(BaseModel::getName).setHeader("Specializations");
//        grid.addColumn(baseModel -> baseModel.getClass().getSimpleName()).setHeader("Category");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setHeight("70vh");

        grid.addSelectionListener(event -> {
            Set<T> selectedItems = event.getAllSelectedItems().stream()
                    .filter(clazz::isInstance)
                    .map(clazz::cast)
                    .collect(Collectors.toSet());

            manageButtons.setSelectedItems(selectedItems);
        });

        add(grid);
        ((BaseModelTreeGrid)grid).expandAll();
    }

    protected void configureManageButtons() {
        manageButtons.addOnSuccessfulModifyListener(this::updateGrid);
    }

    protected void updateGrid() {
        ((BaseModelTreeGrid)grid).updateDataAndExpandAll();
    }

    private T createNewInstanceOfT(){
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
