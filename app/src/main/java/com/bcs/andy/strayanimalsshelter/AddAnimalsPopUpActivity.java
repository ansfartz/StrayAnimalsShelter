package com.bcs.andy.strayanimalsshelter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.models.Animal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddAnimalsPopUpActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private DatabaseReference animalsForUserRef;

    private FloatingActionButton acceptButton;
    private FloatingActionButton deleteButton;
    private EditText newAnimalName;
    private EditText newAnimalAge;
    private EditText newAnimalSpecies;
    private EditText newAnimalObservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animals_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
//        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.65));
        getWindow().setLayout(width, height);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        animalsForUserRef = FirebaseDatabase.getInstance().getReference("users");

        acceptButton = (FloatingActionButton) findViewById(R.id.acceptBtn);
        deleteButton = (FloatingActionButton) findViewById(R.id.deleteBtn);
        newAnimalName = (EditText) findViewById(R.id.newAnimalNameET);
        newAnimalAge = (EditText) findViewById(R.id.newAnimalAgeET);
        newAnimalSpecies = (EditText) findViewById(R.id.newAnimalSpeciesET);
        newAnimalObservations = (EditText) findViewById(R.id.newAnimalObsET);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = firebaseUser.getUid();
                String animalName = newAnimalName.getText().toString().trim();
                String animalSpecies = newAnimalSpecies.getText().toString().trim();
                String animalObservations = newAnimalObservations.getText().toString().trim();
                Integer animalAge = Integer.parseInt(newAnimalAge.getText().toString().trim());
                Animal animal = new Animal(animalName,animalSpecies,animalAge, animalObservations);

                animalsForUserRef.child(uid).child("animals").child(UUID.randomUUID().toString()).setValue(animal);

                finish();
            }
        });




    }
}
