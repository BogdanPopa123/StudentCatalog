package com.packagename.myapp.views;


import com.packagename.myapp.Application;
import com.packagename.myapp.dao.ProfessorRepository;
import com.packagename.myapp.dao.ProfileRepository;
import com.packagename.myapp.dao.SubjectRepository;
import com.packagename.myapp.models.Grade;
import com.packagename.myapp.models.Profile;
import com.packagename.myapp.models.Subject;
import com.packagename.myapp.models.User;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.customComponents.manageButtons.ModifyDialog;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Objects;

@Route(value = "grade", layout = MainLayout.class)
@PageTitle("Grade")
@CssImport("./styles/shared-styles.css")
//@CssImport("./styles/grade-view.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class GradeView extends BaseModelView<Grade> {
    public GradeView() {
        super(Grade.class);
    }

    @Override
    protected void addManageButtons() {
        LoginService loginService = Application.getService(LoginService.class);
        ProfessorRepository professorRepository = Application.getService(ProfessorRepository.class);

        User user = loginService.getAuthenticatedUser();
        if (user.isAdmin() || professorRepository.existsById(user.getId())) {
            add(manageButtons);
        }
    }

    @Override
    protected void addGrid() {
        super.addGrid();

        grid.removeAllColumns();

        grid.addColumn(o -> Objects.toString(((Grade) o).getProfile().getStudent().getName(), "empty")).setKey("student").setHeader("Student");
        grid.addColumn(o -> Objects.toString(((Grade) o).getValue(), "empty")).setKey("grade").setHeader("Grade");
        grid.addColumn(o -> Objects.toString(((Grade) o).getSubject().getName(), "empty")).setKey("subject").setHeader("Subject");
    }

    @Override
    protected void configureManageButtons() {
        super.configureManageButtons();

        ModifyDialog<Grade> modifyDialog = manageButtons.getModifyDialog();
        Binder<Grade> binder = modifyDialog.getBinder();

        modifyDialog.removeAllFields();

        IntegerField grade = new IntegerField("Grade");
        ComboBox<Profile> profile = new ComboBox<>("Profile");
        ComboBox<Subject> subject = new ComboBox<>("Subject");

        profile.setItemLabelGenerator(Profile::getFullName);
        subject.setItemLabelGenerator(Subject::getName);

        profile.setItems(Application.getService(ProfileRepository.class).findAll());
        subject.setItems(Application.getService(SubjectRepository.class).findAll());

        binder.forField(grade)
                .withValidator(g -> g > 0 && g < 11, "Enter a valid grade (1-10)")
                .bind(Grade::getValue, Grade::setValue);

        binder.forField(profile)
                .bind(Grade::getProfile, Grade::setProfile);

        binder.forField(subject)
                .bind(Grade::getSubject, Grade::setSubject);

        modifyDialog.addFields(grade, profile, subject);
    }
}
