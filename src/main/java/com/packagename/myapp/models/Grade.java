package com.packagename.myapp.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "grade")
public class Grade extends BaseModel {

    @Temporal(TemporalType.TIMESTAMP)
    private final Date createdAt = new Date();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private int id;

    @NotNull
    private int mark;

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

    @Override
    public String getName() {
        return Integer.toString(id);
    }

    @Override
    public void setName(String name) {

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

    @Override
    public BaseModel getParent() {
        return null;
    }

    @Override
    public void setParent(BaseModel parent) {

    }

    @Override
    public List<BaseModel> getChildren() {
        return null;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
