package com.packagename.myapp.dao;

import com.packagename.myapp.models.LearningPlan;
import org.springframework.data.repository.CrudRepository;

public interface LearningPlanRepository extends CrudRepository<LearningPlan, Integer> {
}
