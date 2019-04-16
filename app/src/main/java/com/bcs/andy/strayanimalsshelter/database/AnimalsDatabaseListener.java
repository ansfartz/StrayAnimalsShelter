package com.bcs.andy.strayanimalsshelter.database;

import com.bcs.andy.strayanimalsshelter.model.Animal;

import java.util.List;

public interface AnimalsDatabaseListener {

    void onCallback(List<Animal> list);

}
