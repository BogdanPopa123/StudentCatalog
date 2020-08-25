package com.packagename.myapp.dao;

import com.packagename.myapp.models.Faculty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface FacultyRepository extends CrudRepository<Faculty, Integer> {

    boolean existsByName(String name);

    boolean existsById(int id);

    Faculty findById(int id);

    List<Faculty> findAll();
}
