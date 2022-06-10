package com.example.connect_a_britadmin.Database;

public class ClubData {

    String email,gender,key;

    public ClubData() {
    }

    public ClubData(String email, String gender, String key) {
        this.email = email;
        this.gender = gender;
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
