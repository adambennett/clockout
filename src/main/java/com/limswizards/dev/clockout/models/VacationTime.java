package com.limswizards.dev.clockout.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;

@Entity
public class VacationTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer vacation = 0;
    private Integer floating = 0;
    private Integer personal = 0;
    private String username;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "time")
    @JsonIgnoreProperties("time")
    private User user;

    public VacationTime() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVacation() {
        return vacation;
    }

    public void setVacation(Integer vacation) {
        this.vacation = vacation;
    }

    public Integer getFloating() {
        return floating;
    }

    public void setFloating(Integer floating) {
        this.floating = floating;
    }

    public Integer getPersonal() {
        return personal;
    }

    public void setPersonal(Integer personal) {
        this.personal = personal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return this.user.getUsername();
    }

    public void setUsername(String username) {
        this.username = this.user.getUsername();
    }
}
