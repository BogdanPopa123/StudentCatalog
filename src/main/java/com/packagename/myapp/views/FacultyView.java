package com.packagename.myapp.views;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.models.Faculty;
import com.packagename.myapp.views.layout.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;
import java.util.List;

@Route(value = "faculty", layout = MainLayout.class)
@PageTitle("Faculty")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/faculty-view.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class FacultyView extends VerticalLayout {

    private final FacultyRepository facultyRepository;
    private Grid<Faculty> facultyGrid;
    private List<Faculty> faculties;

    public FacultyView(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @PostConstruct
    private void init() {
        addClassName("faculty-view");

        setupHeader();
        setupGrid();

        TextField facultyNameField = new TextField("Faculty");
        facultyNameField.addThemeName("bordered");
        facultyNameField.addClassName("faculty-name-field");

        Button addFaculty = new Button("Add", event -> {
            String facultyName = facultyNameField.getValue();

            if (!Strings.isNullOrEmpty(facultyName) && !facultyRepository.existsByName(facultyName) && faculties.stream().noneMatch(faculty -> faculty.getName().equals(facultyName))) {

                Faculty faculty = new Faculty();
                faculty.setName(facultyName);

                facultyRepository.save(faculty);

                faculties.add(faculty);
                facultyGrid.setItems(faculties);

            } else {
                Notification.show("Wrong faculty name");
            }

        });

        facultyNameField.addKeyPressListener(Key.ENTER, event -> addFaculty.click());

        HorizontalLayout facultyForm = new HorizontalLayout(facultyNameField, addFaculty);
        facultyForm.addClassName("faculty-form");

        facultyForm.setVerticalComponentAlignment(Alignment.BASELINE, facultyNameField);
        facultyForm.setVerticalComponentAlignment(Alignment.BASELINE, addFaculty);

        add(facultyForm);
    }

    private void setupHeader() {
        H1 header = new H1("Faculties");
        header.addClassName("faculty-header");
        add(header);
    }

    private void setupGrid() {
        faculties = Lists.newArrayList(facultyRepository.findAll().iterator());

        facultyGrid = new Grid<>();
        facultyGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        facultyGrid.setItems(faculties);

        facultyGrid.addColumn(Faculty::getId).setHeader("Id").setKey("id").setWidth("10px");
        facultyGrid.addColumn(Faculty::getName).setHeader("Name").setKey("name").setWidth("20px");

        add(facultyGrid);
    }


}
