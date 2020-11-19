package com.packagename.myapp.views;

import com.packagename.myapp.models.BaseModel;
import com.packagename.myapp.models.Department;
import com.packagename.myapp.models.LearningPlan;
import com.packagename.myapp.views.customComponents.BaseModelTreeGrid;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Route(value = "learningPlan", layout = MainLayout.class)
@PageTitle("Learning Plan")
@CssImport("./styles/shared-styles.css")
public class LearningPlanView extends BaseModelView<LearningPlan> {

    private BaseModelTreeGrid grid;
    private final List<CrudRepository<? extends BaseModel, Integer>> repositories;

    public LearningPlanView(List<CrudRepository<? extends BaseModel, Integer>> repositories) {

        super(LearningPlan.class);

        this.repositories = repositories;
    }

}
