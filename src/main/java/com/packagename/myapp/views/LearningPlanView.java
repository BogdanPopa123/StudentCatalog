package com.packagename.myapp.views;

import com.packagename.myapp.dao.LearningPlanRepository;
import com.packagename.myapp.models.BaseModel;
import com.packagename.myapp.models.Department;
import com.packagename.myapp.models.LearningPlan;
import com.packagename.myapp.views.BaseModelView;
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

@Route(value = "learningPlan", layout = MainLayout.class)
@PageTitle("Learning Plan")
@CssImport("./styles/shared-styles.css")
public class LearningPlanView extends BaseModelView<LearningPlan> {

    private final LearningPlanRepository learningPlanRepository;
    private BaseModelTreeGrid grid;
    private final List<CrudRepository<? extends BaseModel, Integer>> repositories;


    public LearningPlanView(LearningPlanRepository learningPlanRepository) {

        super(LearningPlan.class);
        this.learningPlanRepository = learningPlanRepository;
         repositories = Arrays.asList(learningPlanRepository);
    }

    @Override
    protected void addHeader() {
        H1 header = new H1("Learning Plan");
        add(header);
    }

    @Override
    protected void addGrid() {
        grid = new BaseModelTreeGrid(repositories);
        grid.addHierarchyColumn(BaseModel::getName).setHeader("Learning Plans");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setHeight("70vh");

        grid.addSelectionListener(event -> {
            Set<LearningPlan> learningPlans = event.getAllSelectedItems().stream()
                    .filter(item -> item instanceof LearningPlan)
                    .map(item -> (LearningPlan) item)
                    .collect(Collectors.toSet());
            manageButtons.setSelectedItems(learningPlans);
        });
        add(grid);
        grid.expandAll();
    }

    @Override
    protected  void configureManageButtons(){
        manageButtons.addOnSuccessfulModifyListener(this::updateGrid);
    }

    @Override
    protected void updateGrid() {
        grid.updateDataAndExpandAll();
    }
}
