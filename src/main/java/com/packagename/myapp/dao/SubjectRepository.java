package com.packagename.myapp.dao;

import com.packagename.myapp.models.Subject;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubjectRepository extends CrudRepository<Subject, Integer> {
    boolean existsByName(String name);

    List<Subject> findAll();
}
