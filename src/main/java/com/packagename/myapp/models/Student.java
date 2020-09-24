package com.packagename.myapp.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "student_id")
@Table(name = "student")


public class Student extends User  {

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private Set<Profile> profiles;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private Set<Grade> grades;

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    public Set<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }
}


