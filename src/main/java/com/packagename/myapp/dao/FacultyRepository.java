package com.packagename.myapp.dao;

import com.packagename.myapp.models.Faculty;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface FacultyRepository extends CrudRepository<Faculty, Integer> {

    boolean existsByName(String name);
}
