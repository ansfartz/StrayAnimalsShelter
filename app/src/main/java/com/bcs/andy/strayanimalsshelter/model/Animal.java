package com.bcs.andy.strayanimalsshelter.model;

public class Animal {

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
                "animalName='" + animalName + '\'' +
                ", species='" + species + '\'' +
                ", observations='" + observations + '\'' +
                ", aproxAge=" + aproxAge +
                ", adult=" + adult +
                ", neutered=" + neutered +
                '}';
    }
}
