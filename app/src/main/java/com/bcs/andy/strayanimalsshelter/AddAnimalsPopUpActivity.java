package com.bcs.andy.strayanimalsshelter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.bcs.andy.strayanimalsshelter.model.Animal;
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
    private EditText newAnimalObservations;
    private Spinner newAnimalSpecies;

    private void init() {
        newAnimalName = (EditText) findViewById(R.id.newAnimalNameET);
        newAnimalAge = (EditText) findViewById(R.id.newAnimalAgeET);
        newAnimalObservations = (EditText) findViewById(R.id.newAnimalObsET);

        newAnimalSpecies = (Spinner) findViewById(R.id.animalTypesSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types_of_animals, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newAnimalSpecies.setAdapter(adapter);
//        newAnimalType.setOnItemSelectedListener(this);
    }

    private void initFirebase() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        animalsForUserRef = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animals_pop_up);
        setTitle("Add animal");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        initFirebase();
        init();

    }


    // for the Toolbar  that I created, to be used
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.addanimalspopup_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // for the onClickListeners on toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tlb1save:
                saveTlbLogic();
                return true;
            case R.id.tlb1cancel:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveTlbLogic() {
        if (!validate()) {
            Toast.makeText(AddAnimalsPopUpActivity.this, "Please check field errors and try again", Toast.LENGTH_SHORT).show();
        } else {
            String userUid = firebaseUser.getUid();
            String animalName = newAnimalName.getText().toString().trim();
            String animalSpecies = newAnimalSpecies.getSelectedItem().toString();
            String animalObservations = newAnimalObservations.getText().toString().trim();
            Integer animalAge = Integer.parseInt(newAnimalAge.getText().toString().trim());
            Animal animal = new Animal(animalName, animalSpecies, animalAge, animalObservations);
            animalsForUserRef.child(userUid).child("animals").child(UUID.randomUUID().toString()).setValue(animal);

            finish();
        }
    }

    public boolean validate() {
        String animalName = newAnimalName.getText().toString().trim();
        if (newAnimalAge.getText().toString().isEmpty()) {
            newAnimalAge.setError("Please enter an age");
        }
        if (animalName.isEmpty() || animalName.length() > 13) {
            newAnimalName.setError("Please write a name");
            return false;
        }
        return true;
    }
}
