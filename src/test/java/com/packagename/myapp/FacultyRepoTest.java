package com.packagename.myapp;

import com.packagename.myapp.dao.FacultyRepository;
import com.packagename.myapp.models.Faculty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class FacultyRepoTest {

    @Autowired
    FacultyRepository repository;

    @Test
    public void testSaveFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Test faculty");
        faculty.setAbbreviation("T1");

        repository.save(faculty);

        Faculty testFaculty = repository.findByName(faculty.getName());

        Assert.notNull(testFaculty, "Faculty not saved");

        Assert.isTrue(faculty.getName().equals(testFaculty.getName()), "Faculty not found");

        repository.delete(faculty);

        testFaculty = repository.findByName(faculty.getName());

        Assert.isNull(testFaculty, "Faculty was not deleted");
    }
}
