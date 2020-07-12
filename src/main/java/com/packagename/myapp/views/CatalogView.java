package com.packagename.myapp.views;

import com.packagename.myapp.views.layout.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;

@Route(value = "catalog", layout = MainLayout.class)
@PageTitle("Catalog")
@CssImport("./styles/shared-styles.css")
public class CatalogView extends VerticalLayout {
    public CatalogView() {
    }

    @PostConstruct
    private void init() {
        add(new H5("Catalog"));
    }
}
