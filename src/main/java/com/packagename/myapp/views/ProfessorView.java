package com.packagename.myapp.views;

import com.packagename.myapp.dao.DepartmentRepository;
import com.packagename.myapp.dao.ProfessorRepository;
import com.packagename.myapp.dao.UserRepository;
import com.packagename.myapp.models.Department;
import com.packagename.myapp.models.Professor;
import com.packagename.myapp.models.UserRole;
import com.packagename.myapp.services.LoginService;
import com.packagename.myapp.views.customComponents.UserForm;
import com.packagename.myapp.views.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;
import java.util.List;

@Route(value = "professors", layout = MainLayout.class)
@PageTitle("Professor list")
@CssImport("./styles/shared-styles.css")
public class ProfessorView extends VerticalLayout {

    private final LoginService loginService;
    private final DepartmentRepository departmentRepository;
    private final ProfessorRepository professorRepository;
    private final UserRepository userRepository;

    public ProfessorView(LoginService loginService, DepartmentRepository departmentRepository,
                         ProfessorRepository professorRepository, UserRepository userRepository) {
        this.loginService = loginService;
        this.departmentRepository = departmentRepository;
        this.professorRepository = professorRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void init() {

        Grid<Professor> grid = new Grid<>(Professor.class);

        if (loginService.getAuthenticatedUser().isAdmin()) {


            ComboBox<Department> departmentComboBox = new ComboBox<>();
            departmentComboBox.setLabel("Professor's department");
            List<Department> departments = departmentRepository.findAll();
            departmentComboBox.setItemLabelGenerator(Department::getName);
            departmentComboBox.setItems(departments);
            departmentComboBox.setValue(departments.get(0));

            Button addProfessor = new Button("Add professor", e -> {


                Professor professor = new Professor();
                professor.setDepartment(departmentComboBox.getValue());
                professor.setRole(UserRole.TEACHER);

                Runnable onclose = ()->{
                    professorRepository.save(professor);
                    Notification.show("Professor saved successfully!");
                    List<Professor> professors = professorRepository.findAll();
                    grid.setItems(professors);
                };
                UserForm dialogBox = new UserForm(loginService, userRepository, professor, onclose);

                dialogBox.open();

                departmentComboBox.setValue(departments.get(0));
            });

            VerticalLayout adminLayout = new VerticalLayout();
            adminLayout.add(departmentComboBox,
                    addProfessor
            );
            add(adminLayout, grid);
        }

        grid.setItems(professorRepository.findAll());
        grid.setColumns("id", "name", "department");

        add(grid);

    }
}
