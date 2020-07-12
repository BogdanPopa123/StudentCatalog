package com.packagename.myapp.views.main;

import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.catalog.CatalogView;
import com.packagename.myapp.views.contulmeu.MyAccountView;
import com.packagename.myapp.views.home.HomeView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import javax.annotation.PostConstruct;

@CssImport("./styles/main-layout-styles.css")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class MainLayout extends AppLayout {

    private final LoginService loginService;

    public MainLayout(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostConstruct
    private void init() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H3 logo = new H3(String.format("Welcome, %s!", loginService.getAuthenticatedUser().getUsername()));
        logo.addClassName("logo");

        Button logoutButton = new Button("Logout");
        logoutButton.addClassName("logout-button");
        logoutButton.addClickListener(event -> {
            loginService.logout();
            UI.getCurrent().getPage().reload();
        });

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setWidth("100%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        header.add(new DrawerToggle(), logo, logoutButton);
        addToNavbar(header);
    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                new H5("Menu"),
                new RouterLink("Home", HomeView.class),
                new RouterLink("Contul meu", MyAccountView.class),
                new RouterLink("Catalog", CatalogView.class)
        ));
    }
}
