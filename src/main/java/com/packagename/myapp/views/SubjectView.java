package com.packagename.myapp.views;

import com.packagename.myapp.models.Subject;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "subject", layout = MainLayout.class)
@PageTitle("Subject")
@CssImport("./styles/shared-styles.css")
@CssImport("./styles/subject-view-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class SubjectView extends BaseModelView<Subject> {
    public SubjectView() {
        super(Subject.class);
    }
}
