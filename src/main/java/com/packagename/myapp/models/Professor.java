
package com.packagename.myapp.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "professor_id")
@Table(name = "professor")
//EXTENDS USER !!!
public class Professor extends User  {

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToMany
    @JoinTable(
            name = "professor_subject",
            joinColumns = {@JoinColumn(name = "professor_id")},
            inverseJoinColumns = {@JoinColumn(name = "subject_id")}
    )
    private Set<Subject> knownSubjects;


    @OneToMany
    private Set<Course> courses;


    //
  //  @OneToOne(cascade = CascadeType.ALL)
  //  @JoinColumn(name="user_id", referencedColumnName = "id")
  //  private User user;



    //

    
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Subject> getKnownSubjects() {
        return knownSubjects;
    }

    public void setKnownSubjects(Set<Subject> knownSubjects) {
        this.knownSubjects = knownSubjects;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}
