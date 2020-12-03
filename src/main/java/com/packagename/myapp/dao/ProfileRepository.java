package com.packagename.myapp.dao;

import com.packagename.myapp.models.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProfileRepository extends CrudRepository<Profile, Integer> {

    boolean existsByName(String name);

    List<Profile> findByName(String name);
}
