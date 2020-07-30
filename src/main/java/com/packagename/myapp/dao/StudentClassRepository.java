package com.packagename.myapp.dao;

import com.packagename.myapp.models.StudentClass;
import org.springframework.data.repository.CrudRepository;

public interface StudentClassRepository extends CrudRepository<StudentClass, Integer> {
}
