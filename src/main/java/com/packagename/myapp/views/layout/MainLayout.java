package com.packagename.myapp.views.layout;

import com.packagename.myapp.models.UserRole;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.AdminPanelView;
import com.packagename.myapp.views.CatalogView;
import com.packagename.myapp.views.HomeView;
import com.packagename.myapp.views.MyAccountView;
import com.vaadin.flow.component.AttachEvent;
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
import com.vaadin.flow.router.HighlightConditions;
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
        RouterLink home = new RouterLink("Home", HomeView.class);
        RouterLink myAccount = new RouterLink("My account", MyAccountView.class);
        RouterLink catalog = new RouterLink("Catalog", CatalogView.class);

//        home.setHighlightCondition(HighlightConditions.sameLocation());
//        myAccount.setHighlightCondition(HighlightConditions.sameLocation());
//        catalog.setHighlightCondition(HighlightConditions.sameLocation());
//
//        home.setHighlightAction((routerLink, highlight) -> routerLink.addClassName("active-link"));
//        myAccount.setHighlightAction((routerLink, highlight) -> routerLink.addClassName("active-link"));
//        catalog.setHighlightAction((routerLink, highlight) -> routerLink.addClassName("active-link"));

        addToDrawer(new VerticalLayout(
                new H5("Menu"),
                home,
                myAccount,
                catalog
        ));

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (UserRole.ADMIN.equals(loginService.getAuthenticatedUser().getRole())) {
            addToDrawer(new VerticalLayout(new RouterLink("AdminPanel", AdminPanelView.class)));
        }
//        // Check current link
//        UI.getCurrent().getPage().executeJs(
//                 "var links = document.getElementsByTagName(\"a\");\n" +
//                            "for (i = 0; i < links.length; i++) {\n" +
//                            "    if (links[i].href === window.location.href) {\n" +
//                            "        links[i].classList.add('active-link')\n" +
//                            "    }\n" +
//                            "}\n");
    }
}
