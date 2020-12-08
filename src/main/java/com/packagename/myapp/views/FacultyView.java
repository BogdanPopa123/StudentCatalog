package com.packagename.myapp.views;

import com.packagename.myapp.Application;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.models.BaseModel;
import com.packagename.myapp.models.Faculty;
import com.packagename.myapp.views.customComponents.BaseModelTreeGrid;
import com.packagename.myapp.views.customComponents.manageButtons.ModifyDialog;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.repository.CrudRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "faculty", layout = MainLayout.class)
@PageTitle("Faculty")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/faculty-view.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class FacultyView extends BaseModelView<Faculty> {

    private final FacultyRepository repository;
//    BaseModelTreeGrid grid;

    public FacultyView(FacultyRepository repository) {
        super(Faculty.class);
        this.repository = repository;
    }

//    @Override
//    protected void addGrid() {
//        super.addGrid();

//        List<CrudRepository<? extends BaseModel, Integer>> repositories = Collections.singletonList(Application.getService(FacultyRepository.class));

//        grid = new BaseModelTreeGrid(repositories);
//
//
//        grid = new BaseModelTreeGrid(Faculty.class);
//        grid = new BaseModelTreeGrid(repositories);

//        grid.addHierarchyColumn(BaseModel::getName).setHeader(getTableNameCapitalized());
//        grid.addColumn(BaseModel -> BaseModel.getClass().getSimpleName()).setHeader("Category");

//        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
//        grid.setHeight("70vh");

//        grid.addSelectionListener(event -> {
//            Set<Faculty> selectedItems = event.getAllSelectedItems().stream()
//                    .filter(Faculty.class::isInstance)
//                    .map(Faculty.class::cast)
//                    .collect(Collectors.toSet());
//
//            manageButtons.setSelectedItems(selectedItems);
//        });

//        add(grid);
//        grid.ex();
//    }

    protected void configureManageButtons() {
//        super.configureManageButtons();

        ModifyDialog<Faculty> modifyDialog = this.manageButtons.getModifyDialog();
        Binder<Faculty> binder = modifyDialog.getBinder();

        TextField abbreviation = new TextField("Abbreviation");

        modifyDialog.addFields(abbreviation);

        binder.forField(abbreviation)
                .asRequired("Enter abbreviation!")
                .withValidator(s -> !repository.existsByAbbreviation(s), "Abbreviation already taken!")
                .bind(Faculty::getAbbreviation, Faculty::setAbbreviation);


//        this.manageButtons.addOnSuccessfulModifyListener(this::updateGrid);
        super.configureManageButtons();
    }

//    @Override
//    protected void updateGrid() {
//        grid.updateDataAndExpandAll();
////        UI.getCurrent().getPage().reload();
//    }
}

