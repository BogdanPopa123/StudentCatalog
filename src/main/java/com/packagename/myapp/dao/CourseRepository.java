package com.packagename.myapp.dao;

import com.packagename.myapp.models.Course;
import com.packagename.myapp.models.LearningPlan;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Integer> {

    List<Course> findCourseByPlan(LearningPlan plan);

    List<Course> findAll();

    boolean existsByName(@NotNull String name);
}
