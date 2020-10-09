package com.packagename.myapp.models;


import com.packagename.myapp.models.annotations.Parent;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "specialization")
public class Specialization extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialization_id")
    private int id;

    @NotNull
    private String name;

    @Parent
    @NotNull(message = "Select domain")
    @ManyToOne
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    @OneToMany(mappedBy = "specialization", fetch = FetchType.EAGER)
    private Set<StudentClass> studentClasses;

    @OneToOne
    private LearningPlan plan;

    public Set<StudentClass> getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(Set<StudentClass> studentClasses) {
        this.studentClasses = studentClasses;
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

    @Override
    public BaseModel getParent() {
        return domain;
    }

    @Override
    public List<BaseModel> getChildren() {
        return null;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public LearningPlan getPlan() {
        return plan;
    }

    public void setPlan(LearningPlan plan) {
        this.plan = plan;
    }
}
