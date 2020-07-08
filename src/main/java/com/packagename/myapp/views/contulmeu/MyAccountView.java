package com.packagename.myapp.views.contulmeu;

import com.packagename.myapp.views.main.MainLayout;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "cont", layout = MainLayout.class)
@PageTitle("Contul meu")
@CssImport("./styles/shared-styles.css")
@Tag("contulmeu-view")
public class MyAccountView extends VerticalLayout {
    public MyAccountView() {
    }
}
