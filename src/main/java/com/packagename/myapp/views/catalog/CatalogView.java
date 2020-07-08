package com.packagename.myapp.views.catalog;

import com.packagename.myapp.views.main.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "catalog", layout = MainLayout.class)
@PageTitle("Catalog")
@CssImport("./styles/shared-styles.css")
public class CatalogView extends VerticalLayout {
    public CatalogView() {
        add(new H5("Text"));
        add(new H5(VaadinSession.getCurrent().getAttribute("user").toString()));
    }
}
