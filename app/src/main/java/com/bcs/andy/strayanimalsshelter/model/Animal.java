package com.bcs.andy.strayanimalsshelter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Animal implements Parcelable {

    private String animalID;
    private String animalName;
    private String species;        // Dog / Cat
    private String observations;
    private Integer aproxAge;
    private Boolean adult;
    private Boolean neutered;
    private String photoLink;
    private Boolean adoptable;
    private String userUid;
    private AdoptionRequest adoptionRequest;


    public Animal() {
    }



    public Animal(Animal animal) {
        this.animalID = animal.getAnimalID();
        this.animalName = animal.getAnimalName();
        this.species = animal.getSpecies();
        this.observations = animal.getObservations();
        this.aproxAge = animal.getAproxAge();
        this.adult = animal.isAdult();
        this.neutered = animal.isNeutered();
        this.photoLink = animal.getPhotoLink();
        this.adoptable = animal.isAdoptable();
        this.userUid = animal.getUserUid();
        this.adoptionRequest = animal.getAdoptionRequest();
    }

    public Animal(String userUid, String animalID, String animalName, String species, Boolean adult, Boolean neutered, Integer aproxAge, String observations, Boolean adoptable) {
        this.animalID = animalID;
        this.animalName = animalName;
        this.species = species;
        this.observations = observations;
        this.aproxAge = aproxAge;
        this.adult = adult;
        this.neutered = neutered;
        this.adoptable = adoptable;
        this.userUid = userUid;
    }

    public AdoptionRequest getAdoptionRequest() {
        return adoptionRequest;
    }

    public void setAdoptionRequest(AdoptionRequest adoptionRequest) {
        this.adoptionRequest = adoptionRequest;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public Boolean isAdoptable() {
        return adoptable;
    }

    public void setAdoptable(Boolean adoptable) {
        this.adoptable = adoptable;
    }

    public String getAnimalID() {
        return animalID;
    }

    public void setAnimalID(String animalID) {
        this.animalID = animalID;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Integer getAproxAge() {
        return aproxAge;
    }

    public void setAproxAge(Integer aproxAge) {
        this.aproxAge = aproxAge;
    }

    public Boolean isAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public Boolean isNeutered() {
        return neutered;
    }

    public void setNeutered(Boolean neutered) {
        this.neutered = neutered;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(animalID, animal.animalID) &&
                Objects.equals(animalName, animal.animalName) &&
                Objects.equals(species, animal.species) &&
                Objects.equals(observations, animal.observations) &&
                Objects.equals(aproxAge, animal.aproxAge) &&
                Objects.equals(adult, animal.adult) &&
                Objects.equals(neutered, animal.neutered) &&
                Objects.equals(photoLink, animal.photoLink) &&
                Objects.equals(adoptable, animal.adoptable) &&
                Objects.equals(userUid, animal.userUid) &&
                Objects.equals(adoptionRequest, animal.adoptionRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animalID, animalName, species, observations, aproxAge, adult, neutered, photoLink, adoptable, userUid, adoptionRequest);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "animalID='" + animalID + '\'' +
                ", animalName='" + animalName + '\'' +
                ", species='" + species + '\'' +
                ", observations='" + observations + '\'' +
                ", aproxAge=" + aproxAge +
                ", adult=" + adult +
                ", neutered=" + neutered +
                ", photoLink='" + photoLink + '\'' +
                ", adoptable=" + adoptable +
                ", userUid='" + userUid + '\'' +
                ", adoptionRequest=" + adoptionRequest +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.animalID);
        dest.writeString(this.animalName);
        dest.writeString(this.species);
        dest.writeString(this.observations);
        dest.writeValue(this.aproxAge);
        dest.writeValue(this.adult);
        dest.writeValue(this.neutered);
        dest.writeString(this.photoLink);
        dest.writeValue(this.adoptable);
        dest.writeString(this.userUid);
        dest.writeParcelable(this.adoptionRequest, flags);
    }

    protected Animal(Parcel in) {
        this.animalID = in.readString();
        this.animalName = in.readString();
        this.species = in.readString();
        this.observations = in.readString();
        this.aproxAge = (Integer) in.readValue(Integer.class.getClassLoader());
        this.adult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.neutered = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.photoLink = in.readString();
        this.adoptable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.userUid = in.readString();
        this.adoptionRequest = in.readParcelable(AdoptionRequest.class.getClassLoader());
    }

    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel source) {
            return new Animal(source);
        }

        @Override
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };
}
