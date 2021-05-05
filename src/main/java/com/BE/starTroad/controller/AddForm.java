package com.BE.starTroad.controller;

public class AddForm {
    private String name;
    private String email;
    private String birth;
    private String major;
    private String interest;
    private String message;

    public String getName() {
        return name;
    }
    public String getEmail() { return email; }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
