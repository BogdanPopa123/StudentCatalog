package com.packagename.myapp.dao;

import com.packagename.myapp.models.LearningPlan;
import com.packagename.myapp.models.Specialization;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface LearningPlanRepository extends CrudRepository<LearningPlan, Integer> {

    List<LearningPlan> findAllBySpecialization(Specialization specialization);

    boolean existsByName(@NotNull String name);
}
