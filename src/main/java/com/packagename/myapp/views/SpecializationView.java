package com.packagename.myapp.views;


import com.packagename.myapp.models.Specialization;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "specialization", layout = MainLayout.class)
@PageTitle("Specialization")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/specialization-style.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class SpecializationView extends BaseModelView<Specialization> {
    public SpecializationView() {
        super(Specialization.class);
    }
}
