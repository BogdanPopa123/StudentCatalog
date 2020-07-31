package com.packagename.myapp.dao;

import com.packagename.myapp.models.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Integer> {
}
