package com.packagename.myapp.models;

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

