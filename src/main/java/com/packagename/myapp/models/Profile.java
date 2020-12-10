package com.packagename.myapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "profile")
public class Profile extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private int id;

    @JsonIgnore
    private Integer studyYear = 1;

    @NotNull
    private String name;

    public Integer getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(Integer studyYear) {
        this.studyYear = studyYear;
    }

    @ManyToOne
    private Student student;

    @ManyToOne
    private StudentClass studentClass;

    @JsonIgnore
    @NotNull
    FormaFinantare financingForm = FormaFinantare.Buget;

    public FormaFinantare getFinancingForm() {
        return financingForm;
    }

    public void setFinancingForm(FormaFinantare financingForm) {
        this.financingForm = financingForm;
    }

    public Statut getStatus() {
        return status;
    }

    public void setStatus(Statut status) {
        this.status = status;
    }

    public TipBursa getScholarshipType() {
        return scholarshipType;
    }

    public void setScholarshipType(TipBursa scholarshipType) {
        this.scholarshipType = scholarshipType;
    }

    @JsonIgnore
    @NotNull
    Statut status = Statut.Inscris;

    @JsonIgnore
    @NotNull
    TipBursa scholarshipType = TipBursa.Niciuna;

    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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

    public StudentClass getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(StudentClass studentClass) {
        this.studentClass = studentClass;
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
}
