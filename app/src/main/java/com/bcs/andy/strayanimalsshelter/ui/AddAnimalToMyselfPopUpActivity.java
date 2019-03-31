package com.bcs.andy.strayanimalsshelter.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddAnimalToMyselfPopUpActivity extends AppCompatActivity {

    // Firebase
    private FirebaseUser firebaseUser;
    private DatabaseReference animalsForUserRef;

    // UI
    private EditText newAnimalName;
    private EditText newAnimalAproxAge;
    private EditText newAnimalObservations;
    private Spinner newAnimalSpecies;
    private CheckBox newAnimalAdultCheckBox;
    private CheckBox newAnimalNeuteredCheckBox;

    private void init() {
        newAnimalName = (EditText) findViewById(R.id.newAnimalNameET);
        newAnimalAproxAge = (EditText) findViewById(R.id.newAnimalAproxAgeET);
        newAnimalObservations = (EditText) findViewById(R.id.newAnimalObsET);
        newAnimalAdultCheckBox = (CheckBox) findViewById(R.id.newAnimalAdultCheckBox);
        newAnimalNeuteredCheckBox = (CheckBox) findViewById(R.id.newAnimalNeuteredCheckBox);

        newAnimalSpecies = (Spinner) findViewById(R.id.newAnimalTypeSpinner);

//        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.types_of_animals, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.types_of_animals, R.layout.spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newAnimalSpecies.setAdapter(spinnerAdapter);
    }

    private void initFirebase() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        animalsForUserRef = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animal_to_myself_pop_up);
        setTitle("Add Sheltered Animal");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//        getWindow().setLayout((int)(width * .8), (int)(height * .3));

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
            Toast.makeText(AddAnimalToMyselfPopUpActivity.this, "Please check field errors and try again", Toast.LENGTH_SHORT).show();
        } else {
            String userUid = firebaseUser.getUid();
            String animalName = newAnimalName.getText().toString().trim();
            String animalSpecies = newAnimalSpecies.getSelectedItem().toString();
            String animalObservations = newAnimalObservations.getText().toString().trim();
            Integer animalAproxAge = Integer.parseInt(newAnimalAproxAge.getText().toString().trim());
            Boolean isAdult = newAnimalAdultCheckBox.isChecked();
            Boolean isNeutered = newAnimalNeuteredCheckBox.isChecked();
            Animal animal = new Animal(animalName, animalSpecies, isAdult, isNeutered, animalAproxAge, animalObservations);
            animalsForUserRef.child(userUid).child("animals").child(UUID.randomUUID().toString()).setValue(animal);

            finish();
        }
    }

    public boolean validate() {
        String animalName = newAnimalName.getText().toString().trim();
        if (newAnimalAproxAge.getText().toString().isEmpty()) {
            newAnimalAproxAge.setError("Please enter an age");
        }
        if (animalName.isEmpty() || animalName.length() > 13) {
            newAnimalName.setError("Please write a name");
            return false;
        }
        return true;
    }
}
