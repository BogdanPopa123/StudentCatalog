package com.packagename.myapp.views;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.CourseRepository;
import com.packagename.myapp.dao.ProfessorRepository;
import com.packagename.myapp.models.Course;
import com.packagename.myapp.models.Professor;
import com.packagename.myapp.views.customComponents.manageButtons.ModifyDialog;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Set;

@Route(value = "course", layout = MainLayout.class)
@PageTitle("Course")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/course-view-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class CourseView extends BaseModelView<Course> {


    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;
    private Grid<Course> grid;

    public CourseView(CourseRepository courseRepository, ProfessorRepository professorRepository) {
        super(Course.class);
        this.courseRepository = courseRepository;
        this.professorRepository = professorRepository;
    }

    @Override
    protected void addGrid() {
        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.addColumn(Course::getName).setHeader("Name");
        grid.addColumn(course -> course.getSubject().getName()).setHeader("Subject");
        grid.addColumn(course -> course.getProfessor() != null ? course.getProfessor().getName() : "Not assigned").setHeader("Professor");

        grid.addSelectionListener(event -> {
            Set<Course> selectedItems = event.getAllSelectedItems();

            manageButtons.setSelectedItems(selectedItems);
        });

        add(grid);
        updateGrid();
    }

    @Override
    protected void configureManageButtons() {
        ModifyDialog<Course> modifyDialog = this.manageButtons.getModifyDialog();
        Binder<Course> binder = modifyDialog.getBinder();

        ComboBox<Professor> professor = new ComboBox<>("Professor");
        professor.setItemLabelGenerator(Professor::getName);
        professor.setItems(professorRepository.findAll());
        professor.setWidth("300px");

        binder.forField(professor)
                .withValidator(p -> p == null || professorRepository.existsByName(p.getName()), "Select a valid professor")
                .bind(Course::getProfessor, Course::setProfessor);

        modifyDialog.addFields(professor);


        this.manageButtons.addOnSuccessfulModifyListener(this::updateGrid);
    }

    @Override
    protected void updateGrid() {
        ArrayList<Course> courses = Lists.newArrayList(courseRepository.findAll());
        grid.setItems(courses);
    }
}
