package com.packagename.myapp.dao;

import com.packagename.myapp.models.Professor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProfessorRepository extends CrudRepository<Professor, Integer> {

    boolean existsByName(String name);

    Professor save(Professor professor);

    List<Professor> findAll();
}
