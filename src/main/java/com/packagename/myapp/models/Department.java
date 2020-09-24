package com.packagename.myapp.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "department")

public class Department implements  UniversityModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
    private Set<Domain> domains;

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
    private Set<Professor> professors;


    @NotNull
    private String name;

    public Department() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public String getName() {
        return name;
    }

    @Override
    public UniversityModel getParent() {
        return this.getFaculty();
    }

    @Override
    public Collection<UniversityModel> getChildren() {
       // return new ArrayList<UniversityModel>(getDomains());
        // TO DO !!
        return new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<Domain> getDomains() {
        return domains;
    }

    public void setDomains(Set<Domain> domains) {
        this.domains = domains;
    }

    public Set<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(Set<Professor> professors) {
        this.professors = professors;
    }

}
