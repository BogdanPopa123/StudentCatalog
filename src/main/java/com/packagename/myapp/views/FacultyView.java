package com.packagename.myapp.views;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.models.Faculty;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layouts.MainLayout;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Route(value = "faculty", layout = MainLayout.class)
@PageTitle("Faculty")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/faculty-view.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class FacultyView extends VerticalLayoutAuthRestricted {

    private final Logger logger = LogManager.getLogger(FacultyView.class);

    private final FacultyRepository facultyRepository;
    private final LoginService loginService;
    private final Binder<Faculty> binder = new BeanValidationBinder<>(Faculty.class);
    private TextField name = new TextField("Faculty name");
    private TextField abbreviation = new TextField("Abbreviation");
    private Grid<Faculty> facultyGrid;
    private List<Faculty> faculties;
    private Faculty faculty = new Faculty();
    private Dialog dialog;


    public FacultyView(FacultyRepository facultyRepository, LoginService loginService) {
        super(loginService);
        this.facultyRepository = facultyRepository;
        this.loginService = loginService;
    }

    @PostConstruct
    private void init() {
        addClassName("faculty-view");

        setupHeader();
        setupGrid();
        setupFacultyForm();

        setBinder();

    }


    private void setupHeader() {
        H1 header = new H1("Faculties");
        header.addClassName("faculty-header");
        add(header);
    }

    private void setupGrid() {
        faculties = Lists.newArrayList(facultyRepository.findAll());

        facultyGrid = new Grid<>();
        facultyGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        facultyGrid.setItems(faculties);

        facultyGrid.addColumn(Faculty::getId).setHeader("Id").setKey("id").setWidth("10px");
        facultyGrid.addColumn(Faculty::getName).setHeader("Name").setKey("name").setWidth("20px");

        add(facultyGrid);
    }

    private void setupFacultyForm() {
        // Check if user is admin ( only admin can add faculties )
        if (loginService.getAuthenticatedUser().isNotAdmin()) {
            return;
        }

        name.addThemeName("bordered");
        name.addClassName("faculty-name-field");
        name.setRequired(true);

        abbreviation.addThemeName("bordered");
        abbreviation.addClassName("faculty-name-field");
        abbreviation.setRequired(true);

        Button addFaculty = new Button("Add", this::addNewFacultyEvent);

        name.addKeyPressListener(Key.ENTER, event -> addFaculty.click());

        HorizontalLayout facultyForm = new HorizontalLayout(name, abbreviation, addFaculty);
        facultyForm.addClassName("faculty-form");

        facultyForm.setVerticalComponentAlignment(FlexComponent.Alignment.BASELINE, name);
        facultyForm.setVerticalComponentAlignment(FlexComponent.Alignment.BASELINE, abbreviation);
        facultyForm.setVerticalComponentAlignment(FlexComponent.Alignment.BASELINE, addFaculty);

        dialog = new Dialog();
        dialog.add(facultyForm);

        Button addNewFacultyDialog = new Button("Add new faculty", event -> dialog.open());
        add(addNewFacultyDialog);
    }

    /**
     * Call binder.bindInstanceFields(this); after all custom validators
     */
    private void setBinder() {
        binder.setBean(faculty);

        binder.forField(name)
                .asRequired("Enter name!")
                .withValidator(name -> !facultyRepository.existsByName(name) && faculties.stream().noneMatch(faculty -> faculty.getName().equals(name)), "Name already taken!")
                .bind(Faculty::getName, Faculty::setName);

        binder.forField(abbreviation)
                .asRequired("Enter abbreviation!")
                .withValidator(abbreviation -> !facultyRepository.existsByAbbreviation(abbreviation) && faculties.stream().noneMatch(faculty -> faculty.getAbbreviation().equals(abbreviation)), "Abbreviation already taken!")
                .bind(Faculty::getAbbreviation, Faculty::setAbbreviation);

        binder.bindInstanceFields(this);
    }

    private void addNewFacultyEvent(ClickEvent<Button> event) {
        logger.debug("Submit new faculty data");
        if (binder.isValid()) {

            faculty = binder.getBean();

            logger.info("Create new faculty");
            facultyRepository.save(faculty);


            dialog.close();
            updateGrid();

        } else {
            logger.debug("New faculty not valid data");
        }
    }

    private void updateGrid() {
        ArrayList<Faculty> faculties = Lists.newArrayList(facultyRepository.findAll());
        facultyGrid.setItems(faculties);
    }
}

