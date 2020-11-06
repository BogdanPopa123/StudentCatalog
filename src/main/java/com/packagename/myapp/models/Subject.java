package com.packagename.myapp.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "subject")
public class Subject extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private int id;

    @NotNull(message = "Enter name")
    private String name;

    @ManyToMany
    private Set<Professor> professors;

    @OneToMany(mappedBy = "subject", fetch = FetchType.EAGER)
    private Set<Grade> grades;

    @OneToMany(mappedBy = "subject", fetch = FetchType.EAGER)
    private Set<Course> courses;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(Set<Professor> professors) {
        this.professors = professors;
    }

    public Set<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public BaseModel getParent() {
        return null;
    }

    @Override
    public void setParent(BaseModel parent) {}

    @Override
    public List<BaseModel> getChildren() {
        return null;
    }
}
