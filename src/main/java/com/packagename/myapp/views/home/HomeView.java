package com.packagename.myapp.views.home;

import com.packagename.myapp.views.home.HomeView.HomeViewModel;
import com.packagename.myapp.views.main.MainUserView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.templatemodel.TemplateModel;

@Route(value = "home", layout = MainUserView.class)
@PageTitle("Home")
@CssImport("./styles/shared-styles.css")
@Tag("home-view")
public class HomeView extends PolymerTemplate<HomeViewModel> {

    public static interface HomeViewModel extends TemplateModel {
    }

    public HomeView() {
    }
}
