package com.packagename.myapp.dao;

import com.packagename.myapp.models.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Integer> {
}
