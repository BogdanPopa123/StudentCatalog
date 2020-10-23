package com.packagename.myapp.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "grade")
public class Grade {

    @Temporal(TemporalType.TIMESTAMP)
    private final Date createdAt = new Date();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private int id;

    // TODO: 09-Oct-20 Need parents?
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Student student;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Subject subject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
