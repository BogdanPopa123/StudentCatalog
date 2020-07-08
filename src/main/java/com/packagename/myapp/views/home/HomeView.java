package com.packagename.myapp.views.home;

import com.packagename.myapp.views.login.LoginView;
import com.packagename.myapp.views.main.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home")
@CssImport("./styles/shared-styles.css")
public class HomeView extends VerticalLayout implements BeforeEnterObserver {

    public HomeView() {
        add(new H5("Text"));
//        add(new H5(VaadinSession.getCurrent().getAttribute("user").toString()));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        checkUser();
    }


    protected void checkUser() {
        Object user = VaadinSession.getCurrent().getAttribute("user");

        if (user == null) {
            UI.getCurrent().navigate(LoginView.class);
        }
    }
}