package com.bcs.andy.strayanimalsshelter;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bcs.andy.strayanimalsshelter.database.DatabaseService;
import com.bcs.andy.strayanimalsshelter.models.Animal;
import com.bcs.andy.strayanimalsshelter.models.AnimalAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyAnimalsActivity extends AppCompatActivity {

    private ProgressBar myProgressBar;
    private FloatingActionButton addAnimalsBtn;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter animalAdapter;

    private List<Animal> listAnimals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_animals);

        myProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        addAnimalsBtn = (FloatingActionButton) findViewById(R.id.addAnimalsFloatingActionBtn);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); //every item of the RecyclerView has a fixed size;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myProgressBar.setVisibility(View.VISIBLE);

        addAnimalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAnimalsActivity.this, AddAnimalsPopUpActivity.class));
            }
        });


        DatabaseService databaseService = new DatabaseService();
        databaseService.readItemsFromDatabase(new DatabaseService.FirebaseCallback() {
            @Override
            public void onCallback(List<Animal> list) {
                listAnimals.clear();
                listAnimals.addAll(list);
                myProgressBar.setVisibility(View.GONE);
                Log.d("DATABASE_TAG", "MYLIST: " + listAnimals.toString());

                animalAdapter = new AnimalAdapter(listAnimals, MyAnimalsActivity.this);
                recyclerView.setAdapter(animalAdapter);

            }
        });

        // code UNDER readItemsFromDatabase(..) will happen first, because method is asynchronous




        Log.d("DATABASE_TAG", "AFTER FOR: " + listAnimals.toString());




    }
}
