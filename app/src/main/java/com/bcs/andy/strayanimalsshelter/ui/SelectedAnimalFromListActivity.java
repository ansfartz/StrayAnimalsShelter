package com.bcs.andy.strayanimalsshelter.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.squareup.picasso.Picasso;


public class SelectedAnimalFromListActivity extends AppCompatActivity {

    private static final String TAG = "SelectedAnimalFromListActivity";

    private TextView animalNameTV, animalAgeTV, animalObsTV;
    private CheckBox animalAdultCB, animalNeuteredCB;
    private ImageView animalIconIV, animalImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_animal_from_list);

        init();
        setUI();

    }

    private void init() {
        animalNameTV = (TextView) findViewById(R.id.selectedAnimalNameTV);
        animalAgeTV = (TextView) findViewById(R.id.selectedAnimalAgeTV);
        animalObsTV = (TextView) findViewById(R.id.selectedAnimalObsTV);
        animalObsTV.setMovementMethod(new ScrollingMovementMethod());
        animalIconIV = (ImageView) findViewById(R.id.selectedAnimalIconIV);
        animalImageView = (ImageView) findViewById(R.id.selectedAnimalImageView);
        animalAdultCB = (CheckBox) findViewById(R.id.selectedAnimalAdultCheckBox);
        animalNeuteredCB = (CheckBox) findViewById(R.id.selectedAnimalNeuteredCheckBox);
    }

    private void setUI() {
        Animal animal = getIntent().getParcelableExtra("selectedAnimal");
        Log.d(TAG, "setUI: animal: " + animal);

        animalNameTV.setText(animal.getAnimalName());
        animalAgeTV.setText(animal.getAproxAge().toString());
        animalObsTV.setText(animal.getObservations());
        animalAdultCB.setChecked(animal.isAdult());
        animalNeuteredCB.setChecked(animal.isNeutered());

        switch (animal.getSpecies()) {
            case "Dog":
                animalIconIV.setImageResource(R.drawable.dog_icon);
                break;
            case "Cat":
                animalIconIV.setImageResource(R.drawable.cat_icon);
                break;
            default:
                animalIconIV.setImageResource(R.drawable.dog_icon);
                break;
        }

        if (animal.getPhotoLink() != null) {
            Picasso.get().load(animal.getPhotoLink()).fit().into(animalImageView);
        }

    }
}
