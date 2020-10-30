package com.packagename.myapp.models;

import com.packagename.myapp.models.annotations.Parent;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "course")
public class Course extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int id;

    @NotNull
    private String name;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Professor professor;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private LearningPlan plan;

    @Parent
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Subject subject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public LearningPlan getPlan() {
        return plan;
    }

    public void setPlan(LearningPlan plan) {
        this.plan = plan;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public BaseModel getParent() {
        return subject;
    }

    @Override
    public void setParent(BaseModel parent) {
        subject = (Subject) parent;
    }

    @Override
    public List<BaseModel> getChildren() {
        return null;
    }
}
