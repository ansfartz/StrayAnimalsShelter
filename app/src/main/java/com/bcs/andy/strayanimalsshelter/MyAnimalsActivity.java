package com.bcs.andy.strayanimalsshelter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bcs.andy.strayanimalsshelter.database.DatabaseService;
import com.bcs.andy.strayanimalsshelter.models.Animal;
import com.bcs.andy.strayanimalsshelter.models.AnimalAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyAnimalsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter animalAdapter;

    private List<Animal> listAnimals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_animals);

        DatabaseService databaseService = new DatabaseService();
//        databaseService.readData();

        databaseService.readItemsFromDatabase(new DatabaseService.FirebaseCallback() {
            @Override
            public void onCallback(List<Animal> list) {
                listAnimals.addAll(list);
                Log.d("DATABASE_TAG", "MYLIST: " + listAnimals.toString());
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); //every item of the RecyclerView has a fixed size;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for(Integer i = 0; i < 10; i++) {
            Animal animal = new Animal("someName","someSpecies",i,i.toString());
            listAnimals.add(animal);
        }

        Log.d("DATABASE_TAG", "AFTER FOR: " + listAnimals.toString());

        animalAdapter = new AnimalAdapter(listAnimals, this);
        recyclerView.setAdapter(animalAdapter);


    }
}
