package com.packagename.myapp.dao;

import com.packagename.myapp.models.Domain;
import org.springframework.data.repository.CrudRepository;

public interface DomainRepository extends CrudRepository<Domain, Integer> {
}
