package com.packagename.myapp.dao;

import com.packagename.myapp.models.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Integer> {
}
