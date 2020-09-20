package com.packagename.myapp.views;

import com.google.common.collect.Lists;
import com.packagename.myapp.dao.SpecializationRepository;
import com.packagename.myapp.models.Specialization;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layouts.MainLayout;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoThemeDefinition;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Route(value = "specialization", layout = MainLayout.class)
@PageTitle("Specialization")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/specialization-style.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class SpecializationView extends VerticalLayoutAuthRestricted {
    private final SpecializationRepository specializationRepository;

    public SpecializationView(LoginService loginService, SpecializationRepository specializationRepository) {
        super(loginService);
        this.specializationRepository = specializationRepository;
    }

    @PostConstruct
    private void init() {
        addClassName("specialization-view");

        addHeader();
        addGrid();

        Button create = new Button("Create", this::create);
        Button details = new Button("Details", this::details);
        Button modify = new Button("Modify", this::modify);
        Button delete = new Button("Delete", this::delete);

        create.addThemeName("success");
        details.addThemeName("secondary");
        modify.addThemeName("secondary");
        delete.addThemeName("error");

        HorizontalLayout manageButtons = new HorizontalLayout(create, details, modify, delete);
        add(manageButtons);
    }

    private void delete(ClickEvent<Button> event) {

    }

    private void modify(ClickEvent<Button> event) {

    }

    private void details(ClickEvent<Button> event) {

    }

    private void create(ClickEvent<Button> event) {

    }


    private void addHeader() {
        H1 header = new H1("Specialization");
        header.addClassName("specialization-header");
        add(header);
    }

    private void addGrid() {
        ArrayList<Specialization> specializations = Lists.newArrayList(specializationRepository.findAll());

        Grid<Specialization> grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setItems(specializations);

        grid.addColumn(Specialization::getId).setHeader("Id").setKey("id");
        grid.addColumn(Specialization::getName).setHeader("Name").setKey("name");
        grid.addColumn(specialization -> specialization.getDomain().getName()).setHeader("Domain").setKey("domain");

        add(grid);
    }
}
