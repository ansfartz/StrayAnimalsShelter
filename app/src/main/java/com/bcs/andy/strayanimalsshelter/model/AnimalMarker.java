package com.bcs.andy.strayanimalsshelter.model;

import java.util.Objects;

public class AnimalMarker {

    private String markerID;
    private Double longitude;
    private Double latitude;
    private Animal animal;
    private String location;
    private String userUid;


    public AnimalMarker() {
    }

    public AnimalMarker(Double latitude, Double longitude, String location, String userUid) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
        this.userUid = userUid;
    }

    public AnimalMarker(String animalID, Double latitude, Double longitude, String location, String userUid) {
        this.userUid = userUid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
        this.markerID = animalID;
    }

    public AnimalMarker(Double latitude, Double longitude, String location, String userUid, Animal animal) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
        this.userUid = userUid;
        this.animal = animal;
    }

    public AnimalMarker(String location, Double latitude, Double longitude, Animal animal) {
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.animal = animal;
    }

    public String getMarkerID() {
        return markerID;
    }

    public void setMarkerID(String markerID) {
        this.markerID = markerID;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalMarker that = (AnimalMarker) o;
        return Objects.equals(longitude, that.longitude) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(animal, that.animal) &&
                Objects.equals(location, that.location) &&
                Objects.equals(userUid, that.userUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitude, latitude, animal, location, userUid);
    }
}
