package com.bcs.andy.strayanimalsshelter.model;

public class AnimalMarker {

    private Double longitude;
    private Double latitude;
    private Animal animal;
    private String location;
    private String userUid;


    public AnimalMarker() {
    }

    public AnimalMarker(Double latitude, Double longitude, Animal animal, String location, String userUid) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.animal = animal;
        this.location = location;
        this.userUid = userUid;
    }

    public AnimalMarker(Double latitude, Double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public AnimalMarker(Double latitude, Double longitude, Animal animal) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.animal = animal;
    }

    public AnimalMarker(Double latitude, Double longitude, String location, String userUid) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
        this.userUid = userUid;
    }

    public AnimalMarker(String location, Double latitude, Double longitude, Animal animal) {
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.animal = animal;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    @Override
    public String toString() {
        return "AnimalMarker{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", animal=" + animal +
                ", location='" + location + '\'' +
                ", userUid='" + userUid + '\'' +
                '}';
    }
}
