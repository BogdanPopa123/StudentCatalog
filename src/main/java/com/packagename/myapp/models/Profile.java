package com.packagename.myapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "profile")
public class Profile extends BaseModel {

    @JsonIgnore
    @NotNull
    FormaFinantare financingForm = FormaFinantare.Buget;
    @JsonIgnore
    @NotNull
    Statut status = Statut.Inscris;
    @JsonIgnore
    @NotNull
    TipBursa scholarshipType = TipBursa.Niciuna;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private int id;
    @JsonIgnore
    private Integer studyYear = 1;
    @NotNull
    private String name;
    @ManyToOne
    private Student student;
    @OneToMany(mappedBy = "profile", fetch = FetchType.EAGER)
    private Set<Grade> grades;
    @ManyToOne
    private StudentClass studentClass;

    public Integer getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(Integer studyYear) {
        this.studyYear = studyYear;
    }

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

    public Student getStudent() {
        return student != null ? student : Student.getNotExistingStudent();
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

    public Set<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }

    @JsonIgnore
    public String getFullName() {
        return MessageFormat.format("{0} - {1}",
                this.getName(),
                this.getStudent().getFullName());
    }
}
