package com.packagename.myapp.views.main;

import com.packagename.myapp.views.catalog.CatalogView;
import com.packagename.myapp.views.contulmeu.MyAccountView;
import com.packagename.myapp.views.home.HomeView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@CssImport("./styles/shared-styles.css")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }


    private void createHeader() {
        H3 logo = new H3("Bun venit, user!");
        logo.addClassName("logo");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.addClassName("header");
        header.setWidth("100%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink homeLink = new RouterLink("Home", HomeView.class);
        homeLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                new H5("Menu"),
                homeLink,
                new RouterLink("Contul meu", MyAccountView.class),
                new RouterLink("Catalog", CatalogView.class)
        ));
    }
}
