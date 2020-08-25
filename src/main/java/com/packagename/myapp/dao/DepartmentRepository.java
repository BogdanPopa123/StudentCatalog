package com.packagename.myapp.dao;

import com.packagename.myapp.models.Department;
import com.packagename.myapp.models.Faculty;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DepartmentRepository extends CrudRepository<Department, Integer> {
    List<Department> findAllByFaculty_Id(int faculty_id);
}
