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

public class SelectedAnimalFromMarkerActivity extends AppCompatActivity {

    private static final String TAG = "SelectedAnimalFromMarke";

    private TextView animalNameTV, animalAgeTV, animalObsTV;
    private CheckBox animalAdultCB, animalNeuteredCB;
    private ImageView animalIconIV, animalImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_animal_from_marker);

        init();
        setUI();
    }

    private void init() {
        animalNameTV = (TextView) findViewById(R.id.selectedAnimalFromMarkerNameTV);
        animalAgeTV = (TextView) findViewById(R.id.selectedAnimalFromMarkerAgeTV);
        animalObsTV = (TextView) findViewById(R.id.selectedAnimalFromMarkerObsTV);
        animalObsTV.setMovementMethod(new ScrollingMovementMethod());
        animalIconIV = (ImageView) findViewById(R.id.selectedAnimalFromMarkerIconIV);
        animalImageView = (ImageView) findViewById(R.id.selectedAnimalFromMarkerImageView);
        animalAdultCB = (CheckBox) findViewById(R.id.selectedAnimalFromMarkerAdultCheckBox);
        animalNeuteredCB = (CheckBox) findViewById(R.id.selectedAnimalFromMarkerNeuteredCheckBox);
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
