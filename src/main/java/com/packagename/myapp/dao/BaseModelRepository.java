package com.packagename.myapp.dao;

import com.packagename.myapp.models.BaseModel;
import org.springframework.data.repository.CrudRepository;

public interface BaseModelRepository<T extends BaseModel> extends CrudRepository<T, Integer> {

    boolean existsByName(String name);
}
