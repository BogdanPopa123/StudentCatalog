package com.packagename.myapp.dao;

import com.packagename.myapp.models.Specialization;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;

public interface SpecializationRepository extends CrudRepository<Specialization, Integer> {

    boolean existsByName(@NotNull String name);
}
