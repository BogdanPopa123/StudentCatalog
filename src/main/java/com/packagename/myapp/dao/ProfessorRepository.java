package com.packagename.myapp.dao;

import com.packagename.myapp.models.Professor;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface ProfessorRepository extends CrudRepository<Professor, Integer> {
}
