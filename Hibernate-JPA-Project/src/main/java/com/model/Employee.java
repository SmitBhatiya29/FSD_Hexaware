package com.model;

import com.enums.JobTitle;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.lang.model.element.Name;
import java.time.Instant;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String name;
    private String email;

    @Column(name="job_title")
    @Enumerated(EnumType.STRING)
    private JobTitle jobTitle;

    @CreationTimestamp
    @Column(name = "date_of_joining")
    private Instant dateOfJoining;

    @ManyToOne

    private Airline airline;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Instant getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(Instant dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", jobTitle=" + jobTitle +
                ", dateOfJoining=" + dateOfJoining +
                ", airline=" + airline +
                '}';
    }
}
