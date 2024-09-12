package com.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private int age;
    private List<Consommation> consummation_totals;


    public User(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.consummation_totals = new ArrayList<>();
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Consommation> getConsummation() {
        return consummation_totals;
    }

    public void setConsummation_totals(List<Consommation> consummation_totals) {
        this.consummation_totals = consummation_totals;
    }
}
