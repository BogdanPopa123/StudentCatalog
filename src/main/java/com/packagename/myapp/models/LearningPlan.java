package com.packagename.myapp.models;

import com.packagename.myapp.models.annotations.Parent;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "learningPlan")
public class LearningPlan extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String uni_year;

    @NotNull
    private int study_year;

    @NotNull
    private int semester;

    @NotNull
    private double credits;

    @Parent
    @OneToOne
    private Specialization specialization;

    @OneToMany(mappedBy = "plan", fetch = FetchType.EAGER)
    private Set<Course> courses;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public BaseModel getParent() {
        return specialization;
    }

    @Override
    public void setParent(BaseModel parent) {
        this.specialization = (Specialization) parent;
    }

    @Override
    public List<BaseModel> getChildren() {
       return new ArrayList<>(courses);
//        return null;
    }

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

    public String getUni_year() {
        return uni_year;
    }

    public void setUni_year(String uni_year) {
        this.uni_year = uni_year;
    }

    public int getStudy_year() {
        return study_year;
    }

    public void setStudy_year(int study_year) {
        this.study_year = study_year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}

