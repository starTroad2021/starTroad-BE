package com.BE.starTroad.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="like")
public class Like {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;
    private int roadmap_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRoadmap_id() {
        return roadmap_id;
    }

    public void setRoadmap_id(int roadmap_id) {
        this.roadmap_id = roadmap_id;
    }
}
