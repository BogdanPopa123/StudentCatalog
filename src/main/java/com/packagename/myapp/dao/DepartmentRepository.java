package com.packagename.myapp.dao;

import com.packagename.myapp.models.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface DepartmentRepository extends CrudRepository<Department, Integer> {

    boolean existsByName(String name);

    Department save(Department department);

    boolean existsById(Integer id);

    List<Department> findAll();

   // @Query("SELECT D FROM Department D, Faculty F WHERE D.faculty_id = ?1 AND D.faculty_id = F.faculty_id")
   // Collection<Department> findAllByFaculty(int faculty_id);

    Collection<Department> findAllByFaculty_Id(int faculty_id);





}
