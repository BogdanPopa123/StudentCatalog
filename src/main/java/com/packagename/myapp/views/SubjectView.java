package com.packagename.myapp.views;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.SubjectRepository;
import com.packagename.myapp.models.Subject;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Set;

@Route(value = "subject", layout = MainLayout.class)
@PageTitle("Subject")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/subject-view-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class SubjectView extends BaseModelView<Subject> {

    private final SubjectRepository subjectRepository;

    private Grid<Subject> grid;

    public SubjectView(SubjectRepository subjectRepository) {
        super(Subject.class);
        this.subjectRepository = subjectRepository;

        addClassName("subject-view");
    }

    protected void addHeader() {
        H2 header = new H2("Subject");
        header.addClassName("subject-view-header");

        add(header);
    }

    protected void addGrid() {
        grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        grid.addColumn(Subject::getId).setHeader("Id");
        grid.addColumn(Subject::getName).setHeader("Name");

        grid.addSelectionListener(event -> {
            Set<Subject> selectedItems = event.getAllSelectedItems();

            manageButtons.setSelectedItems(selectedItems);
        });

        add(grid);
        updateGrid();
    }

    protected void configureManageButtons() {
        manageButtons.addOnSuccessfulModifyListener(this::updateGrid);
    }

    protected void updateGrid() {
        ArrayList<Subject> subjects = Lists.newArrayList(subjectRepository.findAll());
        grid.setItems(subjects);
    }
}
