package com.packagename.myapp;

import com.packagename.myapp.dao.DepartmentRepository;
import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.models.Department;
import com.packagename.myapp.models.Domain;
import com.packagename.myapp.models.Faculty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;

@SpringBootTest
public class DepartmentTest {

    @Autowired
    DepartmentRepository repository;

    @Autowired
    FacultyRepository facultyRepository;

    @Test
    public void testSaveDepartment(){

        Faculty faculty = new Faculty();
        faculty.setName("Test faculty");
        faculty.setAbbreviation("T1");

        facultyRepository.deleteByName(faculty.getName());

        facultyRepository.save(faculty);

        Faculty testFaculty = facultyRepository.findByName(faculty.getName());

        Assert.notNull(testFaculty, "Faculty not saved");

        Assert.isTrue(faculty.getName().equals(testFaculty.getName()), "Faculty not found");

        Department department = new Department();

        department.setName("department");
        department.setFaculty(testFaculty);

        repository.save(department);

        Department testDepartment = repository.findByName(department.getName());

        Assert.notNull(testDepartment, "Department not saved");

        Assert.isTrue(department.getName().equals(testDepartment.getName()), "Department not found");

        repository.delete(department);

        testDepartment = repository.findByName(department.getName());

        Assert.isNull(testDepartment, "Department not saved");

        facultyRepository.delete(faculty);

        testFaculty = facultyRepository.findByName(faculty.getName());

        Assert.isNull(testFaculty, "Faculty was not deleted");

    }
}
