package com.packagename.myapp.views;

import com.packagename.myapp.views.layout.MainLayout;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;

@Route(value = "account", layout = MainLayout.class)
@PageTitle("My account")
@CssImport("./styles/shared-styles.css")
public class MyAccountView extends VerticalLayout {
    public MyAccountView() {
    }

    @PostConstruct
    private void init() {
        add(new H5("My Account"));
        add(new TextField("Text"));
    }
}