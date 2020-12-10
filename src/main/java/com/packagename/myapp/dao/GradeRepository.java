package com.packagename.myapp.dao;

import com.packagename.myapp.models.Grade;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GradeRepository extends CrudRepository<Grade, Integer> {
    List<Grade> findALlByStudent_Id(int student_id);
}
