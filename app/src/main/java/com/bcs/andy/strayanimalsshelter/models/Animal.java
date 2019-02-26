package com.bcs.andy.strayanimalsshelter.models;

public class Animal {

    private String animalName;
    private String species;        // Dog / Cat / Bird
    private int age;
    private String observations;

    public Animal() {
    }

    public Animal(String animalName, String species, int age, String observations) {
        this.animalName = animalName;
        this.species = species;
        this.age = age;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "animalName='" + animalName + '\'' +
                ", species='" + species + '\'' +
                ", age=" + age +
                ", observations='" + observations + '\'' +
                '}';
    }
}
