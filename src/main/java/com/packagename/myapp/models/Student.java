package com.packagename.myapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "student_id")
@Table(name = "student")
public class Student extends User {

    @JsonIgnore
    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private Set<Profile> profiles;


    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    @JsonIgnore
    public static Student getNotExistingStudent() {
        Student student = new Student();
        student.setName("null");
        student.setUsername("null");
        student.setSurname("null");
        student.setEmail("null");
        return student;
    }

}


