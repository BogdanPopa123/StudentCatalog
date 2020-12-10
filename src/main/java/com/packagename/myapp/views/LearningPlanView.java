package com.packagename.myapp.views;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.*;
import com.packagename.myapp.models.*;
import com.packagename.myapp.views.customComponents.BaseModelTreeGrid;
import com.packagename.myapp.views.customComponents.manageButtons.ModifyDialog;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.repository.CrudRepository;
import org.vaadin.tatu.TwinColSelect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Route(value = "learningPlan", layout = MainLayout.class)
@PageTitle("Learning Plan")
@CssImport("./styles/shared-styles.css")
public class LearningPlanView extends BaseModelView<LearningPlan> {

    private final CourseRepository courseRepository;
    private final LearningPlanRepository learningPlanRepository;
    private Grid<LearningPlan> grid;
    private final List<CrudRepository<? extends BaseModel, Integer>> repositories;

    public LearningPlanView(FacultyRepository facultyRepository,
                            SpecializationRepository specializationRepository,
                            LearningPlanRepository learningPlanRepository,
                            CourseRepository courseRepository) {
        super(LearningPlan.class);
    this.learningPlanRepository = learningPlanRepository;
    this.courseRepository = courseRepository;
    repositories = Arrays.asList(facultyRepository, learningPlanRepository, courseRepository);
    }

    @Override
    protected void addHeader() {
        H1 header = new H1("Learning Plans");
        add(header);
    }

    @Override
    protected void addGrid() {

        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.addColumn(LearningPlan::getName).setHeader("Name");
        grid.addColumn(plan -> plan.getUni_year()).setHeader("Academic Year");
        grid.addColumn(plan -> plan.getCourses()).setHeader("Course");
        grid.addColumn(plan -> plan.getSpecialization() != null ? plan.getSpecialization().getName() : "Not assigned").setHeader("Specialization");

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

        ComboBox<String> uy = new ComboBox<>("Academic year");
        uy.setItems("2020-2021", "2021-2022");
        uy.setWidth("140px");

        ComboBox<Integer> ay = new ComboBox<>("Study year");
        ay.setItems(1, 2, 3, 4, 5, 6);
        ay.setWidth("140px");

        ComboBox<Integer> semester = new ComboBox<>("Semester");
        semester.setItems(1, 2);
        semester.setWidth("140px");

//        ComboBox<Course> course = new ComboBox<>("Course");
//        course.setItems(courseRepository.findAll());
//        course.setWidth("300px");

        TwinColSelect<Course> courses = new TwinColSelect<>();
        courses.setLabel("Courses");
        courses.setItems(courseRepository.findAll());

        NumberField credits = new NumberField("Credits");
        credits.setHasControls(true);
        credits.setMax(30);
        credits.setMin(0);
        credits.setWidth("140px");

        binder.forField(uy).bind(LearningPlan::getUni_year, LearningPlan::setUni_year);
        binder.forField(ay).bind(LearningPlan::getStudy_year, LearningPlan::setStudy_year);
        binder.forField(semester).bind(LearningPlan::getSemester, LearningPlan::setSemester);
        binder.forField(credits).bind(LearningPlan::getCredits, LearningPlan::setCredits);

        binder.forField(courses)
                .bind(LearningPlan::getCourses, LearningPlan::setCourses);

        HorizontalLayout years = new HorizontalLayout();
        years.add(uy, ay);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(semester, credits);

        VerticalLayout form = new VerticalLayout();

        modifyDialog.addField(years, horizontalLayout);

        form.add(modifyDialog, courses);

        manageButtons.addOnSuccessfulModifyListener(this::updateGrid);
    }

    @Override
    protected void updateGrid() {
        ArrayList<LearningPlan> learningPlans = Lists.newArrayList(learningPlanRepository.findAll());
        grid.setItems(learningPlans);
    }

}
