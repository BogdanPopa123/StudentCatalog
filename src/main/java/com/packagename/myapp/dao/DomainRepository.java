package com.packagename.myapp.dao;

import com.packagename.myapp.models.Domain;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;

public interface DomainRepository extends CrudRepository<Domain, Integer> {

    boolean existsByName(@NotNull String name);

}
