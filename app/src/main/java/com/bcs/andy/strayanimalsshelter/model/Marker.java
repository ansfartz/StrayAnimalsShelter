package com.bcs.andy.strayanimalsshelter.model;

public class Marker {

    private String locationName;
    private Double longitude;
    private Double latitude;

    public Marker() {
    }

    public Marker(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Marker(String locationName, double longitude, double latitude) {
        this.locationName = locationName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
