package com.bcs.andy.strayanimalsshelter.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Animal implements Parcelable {
    
    private String animalID;
    private String animalName;
    private String species;        // Dog / Cat
    private String observations;
    private Integer aproxAge;
    private Boolean adult;
    private Boolean neutered;
    private String photoLink;


    public Animal() {
    }

    public Animal(String animalName, String species, Integer aproxAge, String observations) {
        this.animalName = animalName;
        this.species = species;
        this.aproxAge = aproxAge;
        this.observations = observations;
    }

    public Animal(String animalName, String species, Boolean adult, Boolean neutered, Integer aproxAge, String observations) {
        this.animalName = animalName;
        this.species = species;
        this.adult = adult;
        this.neutered = neutered;
        this.aproxAge = aproxAge;
        this.observations = observations;
    }

    public Animal(String animalID, String animalName, String species, Boolean adult, Boolean neutered, Integer aproxAge, String observations) {
        this.animalID = animalID;
        this.animalName = animalName;
        this.species = species;
        this.adult = adult;
        this.neutered = neutered;
        this.aproxAge = aproxAge;
        this.observations = observations;
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
    }

    public static final Parcelable.Creator<Animal> CREATOR = new Parcelable.Creator<Animal>() {
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
