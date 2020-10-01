package com.packagename.myapp.dao;

import com.packagename.myapp.models.Subject;

public interface SubjectRepository extends BaseModelRepository<Subject> {
    boolean existsByName(String name);
}
