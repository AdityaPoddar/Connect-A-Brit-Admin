package com.example.connect_a_britadmin.Activity.User;

public class UserData {

    public String name,email,password,id,year,department;
    public UserData(){}

    public UserData(String name, String email, String password, String id, String year, String department) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;
        this.year = year;
        this.department = department;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}

