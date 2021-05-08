package com.BE.starTroad.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity(name="talk")
public class Talk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private Timestamp created_at;
    private int talk_roadmap;
    private String talk_writer;
    private String description;
    private int status;

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

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public int getTalk_roadmap() {
        return talk_roadmap;
    }

    public void setTalk_roadmap(int talk_roadmap) {
        this.talk_roadmap = talk_roadmap;
    }

    public String getTalk_writer() {
        return talk_writer;
    }

    public void setTalk_writer(String talk_writer) {
        this.talk_writer = talk_writer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
