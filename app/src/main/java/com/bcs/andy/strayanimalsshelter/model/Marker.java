package com.bcs.andy.strayanimalsshelter.model;

public class Marker {

    private String location;
    private Double longitude;
    private Double latitude;
    private Animal animal;

    public Marker() {
    }

    public Marker(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Marker(Double longitude, Double latitude, Animal animal) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.animal = animal;
    }

    public Marker(String location, Double longitude, Double latitude, Animal animal) {
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

    @Override
    public String toString() {
        return "Marker{" +
                "location='" + location + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", animal=" + animal +
                '}';
    }
}
