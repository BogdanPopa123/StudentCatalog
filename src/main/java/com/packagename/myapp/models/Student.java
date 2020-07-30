package com.packagename.myapp.models;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "student_id")
@Table(name = "student")
public class Student extends User {

    @OneToMany
    private Set<Profile> profiles;

    @OneToMany
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
