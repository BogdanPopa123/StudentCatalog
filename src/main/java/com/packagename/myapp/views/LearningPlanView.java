package com.packagename.myapp.views;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.CourseRepository;
import com.packagename.myapp.dao.LearningPlanRepository;
import com.packagename.myapp.dao.ProfessorRepository;
import com.packagename.myapp.dao.SpecializationRepository;
import com.packagename.myapp.models.*;
import com.packagename.myapp.views.customComponents.BaseModelTreeGrid;
import com.packagename.myapp.views.customComponents.manageButtons.ModifyDialog;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Route(value = "learningPlan", layout = MainLayout.class)
@PageTitle("Learning Plan")
@CssImport("./styles/shared-styles.css")
public class LearningPlanView extends BaseModelView<LearningPlan> {

    private final LearningPlanRepository learningPlanRepository;
    private final SpecializationRepository specializationRepository;
    private Grid<LearningPlan> grid;

    public LearningPlanView(LearningPlanRepository learningPlanRepository,
                            SpecializationRepository specializationRepository) {
        super(LearningPlan.class);
        this.learningPlanRepository = learningPlanRepository;
        this.specializationRepository = specializationRepository;
    }

    @Override
    protected void addGrid() {
        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.addColumn(LearningPlan::getName).setHeader("Name");
//        grid.addColumn(learningPlan -> learningPlan.getSpecialization().getName()).setHeader("Specialization");
        grid.addColumn(learningPlan -> learningPlan.getSpecialization() != null ? learningPlan.getSpecialization().getName() : "Not assigned").setHeader("Specs");

        grid.addSelectionListener(event -> {
            Set<LearningPlan> selectedItems = event.getAllSelectedItems();

            manageButtons.setSelectedItems(selectedItems);
        });

        add(grid);
        updateGrid();
    }

    @Override
    protected void configureManageButtons() {
        ModifyDialog<LearningPlan> modifyDialog = manageButtons.getModifyDialog();
        Binder<LearningPlan> binder = modifyDialog.getBinder();

        ComboBox<Specialization> specs = new ComboBox<>("Specialization");
        specs.setItems(specializationRepository.findAll());
        specs.setWidth("300px");

        binder.forField(specs)
                .withValidator(p -> p == null || specializationRepository.existsByName(p.getName()), "Select a valid specialization")
                .bind(LearningPlan::getSpecialization, LearningPlan::setSpecialization);

        modifyDialog.addField(specs);

        manageButtons.addOnSuccessfulModifyListener(this::updateGrid);
    }

    @Override
    protected void updateGrid() {
        ArrayList<LearningPlan> learningPlans = Lists.newArrayList(learningPlanRepository.findAll());
        grid.setItems(learningPlans);
    }

}
