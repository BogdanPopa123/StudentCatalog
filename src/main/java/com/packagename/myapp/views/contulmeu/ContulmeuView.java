package com.packagename.myapp.views.contulmeu;

import com.packagename.myapp.views.contulmeu.ContulmeuView.ContulmeuViewModel;
import com.packagename.myapp.views.main.MainUserView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;

@Route(value = "cont", layout = MainUserView.class)
@PageTitle("Contul meu")
@CssImport("./styles/shared-styles.css")
@Tag("contulmeu-view")
public class ContulmeuView extends PolymerTemplate<ContulmeuViewModel> {

    public static interface ContulmeuViewModel extends TemplateModel {
    }

    public ContulmeuView() {
    }
}
