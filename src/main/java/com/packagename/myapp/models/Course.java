package com.packagename.myapp.models;

import javax.persistence.*;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Professor professor;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private LearningPlan plan;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Subject subject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
