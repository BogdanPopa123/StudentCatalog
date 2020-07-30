package com.packagename.myapp.dao;

import com.packagename.myapp.models.Subject;
import org.springframework.data.repository.CrudRepository;

public interface SubjectRepository extends CrudRepository<Subject, Integer> {
}
