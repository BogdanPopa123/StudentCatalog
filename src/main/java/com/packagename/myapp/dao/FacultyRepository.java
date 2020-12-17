package com.packagename.myapp.dao;

import com.packagename.myapp.models.Faculty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FacultyRepository extends CrudRepository<Faculty, Integer> {

    boolean existsByName(String name);

    boolean existsById(int id);

    Faculty findById(int id);

    Faculty findByName(String name);

    List<Faculty> findAll();

    boolean existsByAbbreviation(String abbreviation);

    @Transactional
    void deleteByName(String name);


}
