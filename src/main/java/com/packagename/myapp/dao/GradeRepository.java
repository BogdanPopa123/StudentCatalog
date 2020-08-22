package com.packagename.myapp.dao;

import com.packagename.myapp.models.Grade;
import org.springframework.data.repository.CrudRepository;

public interface GradeRepository extends CrudRepository<Grade, Integer> {
}
