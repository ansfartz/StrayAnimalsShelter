package com.bcs.andy.strayanimalsshelter.model;

public class Animal {

    private String animalName;
    private String species;        // Dog / Cat / Rodent / Bird
    private Boolean mature;
    private Boolean neutered;
    private Integer aproxAge;
    private String observations;

    public Animal() {
    }

    public Animal(String animalName, String species, Integer aproxAge, String observations) {
        this.animalName = animalName;
        this.species = species;
        this.aproxAge = aproxAge;
        this.observations = observations;
    }

    public Animal(String animalName, String species, Boolean mature, Boolean neutered, Integer aproxAge, String observations) {
        this.animalName = animalName;
        this.species = species;
        this.mature = mature;
        this.neutered = neutered;
        this.aproxAge = aproxAge;
        this.observations = observations;
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

    public Integer getAproxAge() {
        return aproxAge;
    }

    public void setAproxAge(int aproxAge) {
        this.aproxAge = aproxAge;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Boolean getMature() {
        return mature;
    }

    public void setMature(Boolean mature) {
        this.mature = mature;
    }

    public Boolean getNeutered() {
        return neutered;
    }

    public void setNeutered(Boolean neutered) {
        this.neutered = neutered;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "animalName='" + animalName + '\'' +
                ", species='" + species + '\'' +
                ", aproxAge=" + aproxAge +
                ", observations='" + observations + '\'' +
                '}';
    }
}
