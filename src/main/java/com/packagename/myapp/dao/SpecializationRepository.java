package com.packagename.myapp.dao;

import com.packagename.myapp.models.Specialization;
import org.springframework.data.repository.CrudRepository;

public interface SpecializationRepository extends CrudRepository<Specialization, Integer> {
}
