package com.packagename.myapp.views;

import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;

@Route(value = "catalog", layout = MainLayout.class)
@PageTitle("Catalog")
@CssImport("./styles/shared-styles.css")
public class CatalogView extends VerticalLayoutAuthRestricted {

    public CatalogView(LoginService loginService) {
        super(loginService);
    }

    @PostConstruct
    private void init() {
        add(new H5("Catalog"));
    }
}
