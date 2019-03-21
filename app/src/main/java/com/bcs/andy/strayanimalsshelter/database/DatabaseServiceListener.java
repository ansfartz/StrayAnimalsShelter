package com.bcs.andy.strayanimalsshelter.database;

import com.bcs.andy.strayanimalsshelter.model.Animal;

import java.util.List;

public interface DatabaseServiceListener {

    void onCallback(List<Animal> list);

}
