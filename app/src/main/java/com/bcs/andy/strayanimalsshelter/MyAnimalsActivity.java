package com.bcs.andy.strayanimalsshelter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bcs.andy.strayanimalsshelter.database.DatabaseService;
import com.bcs.andy.strayanimalsshelter.models.Animal;
import com.bcs.andy.strayanimalsshelter.models.AnimalAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyAnimalsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter animalAdapter;

    private List<Animal> listAnimals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_animals);

        DatabaseService databaseService = new DatabaseService();
        databaseService.readData();


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); //every item of the recyclerview has a fixed size;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listAnimals = new ArrayList<>();

        for(Integer i = 0; i < 10; i++) {
            Animal animal = new Animal("someName","someSpecies",i,i.toString());
            listAnimals.add(animal);
        }

        animalAdapter = new AnimalAdapter(listAnimals, this);
        recyclerView.setAdapter(animalAdapter);


    }
}
