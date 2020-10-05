package com.packagename.myapp.dao;

import com.packagename.myapp.models.StudentClass;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentClassRepository extends CrudRepository<StudentClass, Integer> {

    StudentClass save(StudentClass studentClass);

    List<StudentClass> findAll();
}
