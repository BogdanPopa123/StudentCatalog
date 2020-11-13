package com.packagename.myapp.models;


import com.packagename.myapp.models.annotations.Parent;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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

    @NotNull
    private int studyDuration;

    public int getStudyDuration() {
        return studyDuration;
    }

    public void setStudyDuration(int studyDuration) {
        this.studyDuration = studyDuration;
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
    public void setParent(BaseModel parent) {
        this.domain = (Domain) parent;
    }

    @Override
    public List<BaseModel> getChildren() {
//        return new ArrayList<>(plan);
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

    @Override
    public String toString() {
        return "Specialization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", domain=" + domain +
                ", studentClasses=" + studentClasses +
                ", plan=" + plan +
                '}';
    }
}
