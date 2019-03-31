package com.bcs.andy.strayanimalsshelter.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;


// TODO: add Database object and EDIT ANIMAL functionality
public class SelectedAnimalActivity extends AppCompatActivity {

    TextView selectedAnimalNameTV, selectedAnimalAgeTV, selectedAnimalObsTV;
    CheckBox selectedAnimalAdultCB, selectedAnimalNeuteredCB;
    ImageView selectedAnimalIconIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_animal);

        init();
        setUI();

    }

    private void init() {
        selectedAnimalNameTV = (TextView) findViewById(R.id.selectedAnimalNameTV);
        selectedAnimalAgeTV = (TextView) findViewById(R.id.selectedAnimalAgeTV);
        selectedAnimalObsTV = (TextView) findViewById(R.id.selectedAnimalObsTV);
        selectedAnimalObsTV.setMovementMethod(new ScrollingMovementMethod());
        selectedAnimalIconIV = (ImageView) findViewById(R.id.selectedAnimalIconIV);
        selectedAnimalAdultCB = (CheckBox) findViewById(R.id.selectedAnimalAdultCheckBox);
        selectedAnimalNeuteredCB = (CheckBox) findViewById(R.id.selectedAnimalNeuteredCheckBox);
    }

    private void setUI() {
        String name = getIntent().getExtras().getString("selectedAnimalName");
        int age = getIntent().getExtras().getInt("selectedAnimalAge");
        String obs = getIntent().getExtras().getString("selectedAnimalObs");
        String species = getIntent().getExtras().getString("selectedAnimalSpecies");
        boolean adult = getIntent().getExtras().getBoolean("selectedAnimalIsAdult");
        boolean neutered = getIntent().getExtras().getBoolean("selectedAnimalIsNeutered");

        selectedAnimalNameTV.setText(name);
        selectedAnimalAgeTV.setText(String.valueOf(age));
        selectedAnimalObsTV.setText(obs);
        selectedAnimalAdultCB.setChecked(adult);
        selectedAnimalNeuteredCB.setChecked(neutered);


        switch (species) {
            case "Dog":
                selectedAnimalIconIV.setImageResource(R.drawable.dog_icon);
                break;
            case "Cat":
                selectedAnimalIconIV.setImageResource(R.drawable.cat_icon);
                break;
            default:
                selectedAnimalIconIV.setImageResource(R.drawable.dog_icon);
                break;
        }

    }
}
