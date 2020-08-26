package com.packagename.myapp.dao;

import com.packagename.myapp.models.Faculty;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import java.util.stream.Stream;

public interface FacultyRepository extends CrudRepository<Faculty, Integer> {

    boolean existsByName(String name);

    boolean existsByAbbreviation(String abbreviation);
}
