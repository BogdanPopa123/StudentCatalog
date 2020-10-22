package com.packagename.myapp.views;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.models.Faculty;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.customComponents.manageButtons.ManageButtons;
import com.packagename.myapp.views.customComponents.manageButtons.ModifyDialog;
import com.packagename.myapp.views.layouts.MainLayout;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.textfield.TextField;
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
    private final ManageButtons<Faculty> manageButtons = new ManageButtons<>(Faculty.class);

    private Grid<Faculty> facultyGrid;
    private List<Faculty> faculties;


    public FacultyView(FacultyRepository facultyRepository, LoginService loginService) {
        super(loginService);
        this.facultyRepository = facultyRepository;
        this.loginService = loginService;
    }

    @PostConstruct
    private void init() {
        addClassName("faculty-view");

        addHeader();
        addManageButtons();
        addGrid();
        configureManageButtons();
    }


    private void addHeader() {
        H1 header = new H1("Faculties");
        header.addClassName("faculty-header");
        add(header);
    }

    private void addManageButtons() {
        if (loginService.getAuthenticatedUser().isNotAdmin()) {
            return;
        }

        add(manageButtons);
    }

    private void addGrid() {
        faculties = Lists.newArrayList(facultyRepository.findAll());

        facultyGrid = new Grid<>();
        facultyGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        facultyGrid.setItems(faculties);

//        facultyGrid.addColumn(Faculty::getId).setHeader("Id").setKey("id").setWidth("10px");
        facultyGrid.addColumn(Faculty::getName).setHeader("Name").setKey("name").setWidth("20px");
        facultyGrid.addColumn(Faculty::getAbbreviation).setHeader("Abbreviation").setKey("abbreviation").setWidth("20px");

        add(facultyGrid);
    }

    private void configureManageButtons() {
        TextField abbreviation = new TextField("Abbreviation");

        ModifyDialog<Faculty> modifyDialog = manageButtons.getModifyDialog();
        Binder<Faculty> binder = modifyDialog.getBinder();

        modifyDialog.addField(abbreviation);
        binder.forField(abbreviation)
                .asRequired("Enter abbreviation!")
                .withValidator(s -> !facultyRepository.existsByAbbreviation(s) && faculties.stream().noneMatch(faculty -> faculty.getAbbreviation().equals(s)), "Abbreviation already taken!")
                .bind(Faculty::getAbbreviation, Faculty::setAbbreviation);

        manageButtons.addOnSuccessfulModifyListener(this::updateGrid);
    }

    private void updateGrid() {
        ArrayList<Faculty> faculties = Lists.newArrayList(facultyRepository.findAll());
        facultyGrid.setItems(faculties);
    }
}

