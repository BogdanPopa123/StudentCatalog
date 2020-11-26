package com.packagename.myapp.views;


import com.packagename.myapp.Application;
import com.packagename.myapp.dao.StudentRepository;
import com.packagename.myapp.dao.SubjectRepository;
import com.packagename.myapp.models.Grade;
import com.packagename.myapp.models.Student;
import com.packagename.myapp.models.Subject;
import com.packagename.myapp.views.customComponents.manageButtons.ManageButtons;
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
    protected void addGrid() {
        super.addGrid();

        grid.removeAllColumns();

        grid.addColumn(o -> Objects.toString(((Grade) o).getStudent(), "empty")).setKey("student").setHeader("Student");
        grid.addColumn(o -> Objects.toString(((Grade) o).getMark(), "empty")).setKey("grade").setHeader("Grade");
        grid.addColumn(o -> Objects.toString(((Grade) o).getSubject().getName(), "empty")).setKey("subject").setHeader("Subject");
    }

    @Override
    protected void configureManageButtons(ManageButtons<Grade> manageButtons) {
        ModifyDialog<Grade> modifyDialog = manageButtons.getModifyDialog();
        Binder<Grade> binder = modifyDialog.getBinder();

        modifyDialog.removeAllFields();

        IntegerField grade = new IntegerField("Grade");
        ComboBox<Student> student = new ComboBox<>("Student");
        ComboBox<Subject> subject = new ComboBox<>("Subject");

        student.setItemLabelGenerator(Student::getFullName);
        subject.setItemLabelGenerator(Subject::getName);

        student.setItems(Application.getService(StudentRepository.class).findAll());
        subject.setItems(Application.getService(SubjectRepository.class).findAll());

        binder.forField(grade)
                .withValidator(g -> g > 0 && g < 11, "Enter a valid grade (1-10)")
                .bind(Grade::getMark, Grade::setMark);

        binder.forField(student)
                .bind(Grade::getStudent, Grade::setStudent);

        binder.forField(subject)
                .bind(Grade::getSubject, Grade::setSubject);

        modifyDialog.addFields(grade, student, subject);

    }
}
