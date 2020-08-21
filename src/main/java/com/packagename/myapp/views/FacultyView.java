package com.packagename.myapp.views;

import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.models.Faculty;
import com.packagename.myapp.views.layout.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.PostConstruct;
import java.util.List;

@Route(value = "faculty", layout = MainLayout.class)
@PageTitle("Faculty")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/faculty-view.css")
public class FacultyView extends VerticalLayout {

    private final FacultyRepository facultyRepository;

    public FacultyView(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @PostConstruct
    private void init() {
        addClassName("faculty-view");

        setupHeader();
        setupGrid();

    }

    private void setupHeader() {
        H1 header = new H1("Faculties");
        header.addClassName("faculty-header");
        add(header);
    }

    private void setupGrid() {
        List<Faculty> faculties = Lists.newArrayList(facultyRepository.findAll().iterator());

        Grid<Faculty> facultyGrid = new Grid<>();
        facultyGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        facultyGrid.setItems(faculties);

        facultyGrid.addColumn(Faculty::getId).setHeader("Id").setKey("id").setWidth("10px");
        facultyGrid.addColumn(Faculty::getName).setHeader("Name").setKey("name").setWidth("20px");

        add(facultyGrid);
    }


}

