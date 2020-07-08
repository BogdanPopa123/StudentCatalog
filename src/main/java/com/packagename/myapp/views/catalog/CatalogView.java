package com.packagename.myapp.views.catalog;

import com.packagename.myapp.views.catalog.CatalogView.CatalogViewModel;
import com.packagename.myapp.views.main.MainUserView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;

@Route(value = "catalog", layout = MainUserView.class)
@PageTitle("Catalog")
@CssImport("./styles/shared-styles.css")
@Tag("catalog-view")
public class CatalogView extends PolymerTemplate<CatalogViewModel> {

    public interface CatalogViewModel extends TemplateModel {
    }

    public CatalogView() {
    }
}
