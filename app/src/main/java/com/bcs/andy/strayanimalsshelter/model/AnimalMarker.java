package com.bcs.andy.strayanimalsshelter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class AnimalMarker implements Parcelable {

    private String markerID;
    private Double longitude;
    private Double latitude;
    private Animal animal;
    private String location;
    private String userUid;
    private RemovalRequest removalRequest;

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

    public RemovalRequest getRemovalRequest() {
        return removalRequest;
    }

    public void setRemovalRequest(RemovalRequest removalRequest) {
        this.removalRequest = removalRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalMarker that = (AnimalMarker) o;
        return Objects.equals(markerID, that.markerID) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(animal, that.animal) &&
                Objects.equals(location, that.location) &&
                Objects.equals(userUid, that.userUid) &&
                Objects.equals(removalRequest, that.removalRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(markerID, longitude, latitude, animal, location, userUid, removalRequest);
    }

    @Override
    public String toString() {
        return "AnimalMarker{" +
                "markerID='" + markerID + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", animal=" + animal +
                ", location='" + location + '\'' +
                ", userUid='" + userUid + '\'' +
                ", removalRequest=" + removalRequest +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.markerID);
        dest.writeValue(this.longitude);
        dest.writeValue(this.latitude);
        dest.writeParcelable(this.animal, flags);
        dest.writeString(this.location);
        dest.writeString(this.userUid);
        dest.writeParcelable(this.removalRequest, flags);
    }

    protected AnimalMarker(Parcel in) {
        this.markerID = in.readString();
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.animal = in.readParcelable(Animal.class.getClassLoader());
        this.location = in.readString();
        this.userUid = in.readString();
        this.removalRequest = in.readParcelable(RemovalRequest.class.getClassLoader());
    }

    public static final Creator<AnimalMarker> CREATOR = new Creator<AnimalMarker>() {
        @Override
        public AnimalMarker createFromParcel(Parcel source) {
            return new AnimalMarker(source);
        }

        @Override
        public AnimalMarker[] newArray(int size) {
            return new AnimalMarker[size];
        }
    };
}
