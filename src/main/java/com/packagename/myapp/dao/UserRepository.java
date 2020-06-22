package com.packagename.myapp.dao;

import com.packagename.myapp.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmail(String email);

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

}
