package com.bcs.andy.strayanimalsshelter.model;

import java.util.List;
import java.util.Map;

public class User {

    private String uuid;
    private String email;
    private String name;
    private Map<String, Animal> animals;

    public User() {
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User(String uuid, String email, String name) {
        this.uuid = uuid;
        this.email = email;
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(Map<String, Animal> animals) {
        this.animals = animals;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", animals=" + animals +
                '}';
    }
}
