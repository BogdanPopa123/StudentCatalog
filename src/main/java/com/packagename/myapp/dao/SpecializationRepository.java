package com.packagename.myapp.dao;

import com.packagename.myapp.models.Domain;
import com.packagename.myapp.models.Specialization;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface SpecializationRepository extends CrudRepository<Specialization, Integer> {

    boolean existsByName(@NotNull String name);

    List<Specialization> findAllByDomain(Domain domain);
}
