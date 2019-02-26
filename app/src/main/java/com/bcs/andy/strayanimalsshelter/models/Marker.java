package com.bcs.andy.strayanimalsshelter.models;

public class Marker {

    private double longitude;
    private double latitude;
    private String removalRequest_user;

    public Marker() {
    }

    public Marker(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getRemovalRequest_user() {
        return removalRequest_user;
    }

    public void setRemovalRequest_user(String removalRequest_user) {
        this.removalRequest_user = removalRequest_user;
    }
}
