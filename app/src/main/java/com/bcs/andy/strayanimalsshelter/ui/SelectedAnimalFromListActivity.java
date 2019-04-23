package com.bcs.andy.strayanimalsshelter.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.database.AnimalsDatabase;
import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.bcs.andy.strayanimalsshelter.utils.UserUtils;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;


public class SelectedAnimalFromListActivity extends AppCompatActivity {

    private static final String TAG = "SelectedAnimalFromListActivity";

    // vars
    private Animal animal;
    private Animal editedAnimal;
    private Boolean changesBeenMade;

    // UI
    private TextView animalNameTV, animalAgeTV, animalObsTV;
    private CheckBox animalAdultCB, animalNeuteredCB;
    private ImageView animalIconIV, animalImageView, animalAdoptableImageView;
    private FloatingActionButton adoptableFab, makeChangesFab;

    // Firebase
    private AnimalsDatabase animalsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_animal_from_list);

        init();
        setUI();
        getAdoptableDialogBuilder();
        initFabListeners();


    }

    private void init() {
        animalsDatabase = new AnimalsDatabase();

        animal = getIntent().getParcelableExtra("selectedAnimal");
        editedAnimal = new Animal(animal);
        Log.d(TAG, "init: animal = " + animal);

        animalNameTV = (TextView) findViewById(R.id.selectedAnimalNameTV);
        animalAgeTV = (TextView) findViewById(R.id.selectedAnimalAgeTV);
        animalObsTV = (TextView) findViewById(R.id.selectedAnimalObsTV);
        animalObsTV.setMovementMethod(new ScrollingMovementMethod());
        animalIconIV = (ImageView) findViewById(R.id.selectedAnimalIconIV);
        animalImageView = (ImageView) findViewById(R.id.selectedAnimalImageView);
        animalAdultCB = (CheckBox) findViewById(R.id.selectedAnimalAdultCheckBox);
        animalNeuteredCB = (CheckBox) findViewById(R.id.selectedAnimalNeuteredCheckBox);
        animalAdoptableImageView = (ImageView) findViewById(R.id.selectedAnimalAdoptableImageView);
        adoptableFab = (FloatingActionButton) findViewById(R.id.fab_selected_animal_from_list_adoptable);
        makeChangesFab = (FloatingActionButton) findViewById(R.id.fab_selected_animal_from_list_make_change);
    }

    private void setUI() {

        animalNameTV.setText(editedAnimal.getAnimalName());
        animalAgeTV.setText(editedAnimal.getAproxAge().toString());
        animalObsTV.setText(editedAnimal.getObservations());
        animalAdultCB.setChecked(editedAnimal.isAdult());
        animalNeuteredCB.setChecked(editedAnimal.isNeutered());

        switch (editedAnimal.getSpecies()) {
            case "dog":
                animalIconIV.setImageResource(R.drawable.dog_icon);
                break;
            case "cat":
                animalIconIV.setImageResource(R.drawable.cat_icon);
                break;
            default:
                animalIconIV.setImageResource(R.drawable.dog_icon);
                break;
        }

        if (editedAnimal.getPhotoLink() != null) {
            Picasso.get().load(animal.getPhotoLink()).fit().into(animalImageView);
        }

        if (editedAnimal.isAdoptable()) {
            animalAdoptableImageView.setBackgroundResource(R.drawable.round_green_background);
        } else {
            animalAdoptableImageView.setBackgroundResource(R.drawable.round_red_background);
        }


    }


    private void initFabListeners() {

        adoptableFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog adoptableDialog = getAdoptableDialogBuilder().create();
                adoptableDialog.show();
            }
        });


        makeChangesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog editAnimalDialog = getEditAnimalDialogBuilder().create();
                editAnimalDialog.show();
            }
        });
    }


    private AlertDialog.Builder getEditAnimalDialogBuilder() {

        String[] editOptions = {"Name", "Age", "Neutered", "Adult", "Observations", "Species"};

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectedAnimalFromListActivity.this);
        dialogBuilder.setTitle("Edit " + editedAnimal.getAnimalName())
                .setItems(editOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                Dialog nameDialog = getEditNameDialogBuilder().create();
                                nameDialog.show();
                                break;
                            case 1:
                                Dialog ageDialog = getEditAgeDialogBuilder().create();
                                ageDialog.show();
                                break;
                            case 2:
                                Dialog neuteredDialog = getEditNeuteredDialogBuilder().create();
                                neuteredDialog.show();
                                break;
                            case 3:
                                Dialog adultDialog = getEditAdultDialogBuilder().create();
                                adultDialog.show();
                                break;
                            case 4:
                                Dialog obsDialog = getEditObsDialogBuilder().create();
                                obsDialog.show();
                                break;
                            case 5:
                                Dialog speciesDialog = getEditSpeciesDialogBuilder().create();
                                speciesDialog.show();
                                break;
                        }


                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return dialogBuilder;


    }

    private AlertDialog.Builder getEditNameDialogBuilder() {
        final EditText taskEditText = new EditText(SelectedAnimalFromListActivity.this);
        taskEditText.setText(editedAnimal.getAnimalName());
        taskEditText.setGravity(Gravity.CENTER);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectedAnimalFromListActivity.this)
                .setTitle("New name")
                .setMessage("Name should not exceed 13 characters")
                .setView(taskEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = taskEditText.getText().toString().trim().replace("\n", "");
                        if (newName.length() >= 13) {
                            Toast.makeText(SelectedAnimalFromListActivity.this, "Name length over 13 characters.", Toast.LENGTH_SHORT).show();
                        } else if (newName.isEmpty()) {
                            Toast.makeText(SelectedAnimalFromListActivity.this, "Name should not be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            editedAnimal.setAnimalName(newName);
                            animalNameTV.setText(editedAnimal.getAnimalName());
                            Log.d(TAG, "onClick: editedAnimal name = " + editedAnimal.getAnimalName());
                        }

                    }
                })
                .setNegativeButton("Cancel", null);

        return dialogBuilder;
    }

    private AlertDialog.Builder getEditAgeDialogBuilder() {
        final EditText taskEditText = new EditText(SelectedAnimalFromListActivity.this);
        taskEditText.setText(editedAnimal.getAproxAge().toString());
        taskEditText.setGravity(Gravity.CENTER);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectedAnimalFromListActivity.this)
                .setTitle("New aprox age")
                .setMessage("Any age above 0 is fine, but please try to be realistic.")
                .setView(taskEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newAge = taskEditText.getText().toString().trim();
                        if (newAge.startsWith("0")) {
                            Toast.makeText(SelectedAnimalFromListActivity.this, "Age should not start with 0", Toast.LENGTH_SHORT).show();
                        } else if (newAge.isEmpty()) {
                            Toast.makeText(SelectedAnimalFromListActivity.this, "Age should not be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            editedAnimal.setAproxAge(Integer.parseInt(newAge));
                            animalAgeTV.setText(editedAnimal.getAproxAge().toString());
                            Log.d(TAG, "onClick: editedAnimal age = " + editedAnimal.getAproxAge());
                        }

                    }
                })
                .setNegativeButton("Cancel", null);

        return dialogBuilder;
    }

    private AlertDialog.Builder getEditNeuteredDialogBuilder() {
        final boolean originalValue = editedAnimal.isNeutered();
        int checkedItem = editedAnimal.isNeutered() ? 0 : 1;
        CharSequence[] array = {"Yes", "No"};
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectedAnimalFromListActivity.this)
                .setTitle("Has this animal been neutered ?")
                .setSingleChoiceItems(array, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        array = {"Yes", "No"}; ----> (top) "Yes" = 0, (bottom) "No" = 1
                        if (which == 0) {
                            Log.d(TAG, "CHOICE onClick: Editing neutered = true");
                            editedAnimal.setNeutered(true);
                            animalNeuteredCB.setChecked(true);
                        } else {
                            Log.d(TAG, "CHOICE onClick: Editing neutered = false");
                            editedAnimal.setNeutered(false);
                            animalNeuteredCB.setChecked(false);
                        }
                    }

                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "OK onClick: changes OK'ed");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "CANCEL onClick: originalValue = " + originalValue);
                        if(originalValue) {
                            Log.d(TAG, "CANCEL onClick: reverting neutered to true");
                            editedAnimal.setNeutered(true);
                            animalNeuteredCB.setChecked(true);
                        } else {
                            Log.d(TAG, "CANCEL onClick: reverting neutered to false");
                            editedAnimal.setNeutered(false);
                            animalNeuteredCB.setChecked(false);
                        }
                        dialog.dismiss();
                    }
                });

        return dialogBuilder;
    }

    private AlertDialog.Builder getEditAdultDialogBuilder() {
        final boolean originalValue = editedAnimal.isAdult();
        int checkedItem = editedAnimal.isAdult() ? 0 : 1;
        CharSequence[] array = {"Yes", "No"};
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectedAnimalFromListActivity.this)
                .setTitle("Is this an adult animal ?")
                .setSingleChoiceItems(array, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        array = {"Yes", "No"}; ----> (top) "Yes" = 0, (bottom) "No" = 1
                        if (which == 0) {
                            Log.d(TAG, "onClick: Editing adult = true");
                            editedAnimal.setAdult(true);
                            animalAdultCB.setChecked(true);
                        } else {
                            Log.d(TAG, "onClick: Editing adult = false");
                            editedAnimal.setAdult(false);
                            animalAdultCB.setChecked(false);
                        }
                    }

                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "CANCEL onClick: originalValue = " + originalValue);
                        if(originalValue) {
                            Log.d(TAG, "CANCEL onClick: reverting adult to true");
                            editedAnimal.setAdult(true);
                            animalAdultCB.setChecked(true);
                        } else {
                            Log.d(TAG, "CANCEL onClick: reverting adult to false");
                            editedAnimal.setAdult(false);
                            animalAdultCB.setChecked(false);
                        }
                        dialog.dismiss();
                    }
                });

        return dialogBuilder;
    }


    private AlertDialog.Builder getEditObsDialogBuilder() {
        final EditText taskEditText = new EditText(SelectedAnimalFromListActivity.this);
        taskEditText.setText(editedAnimal.getObservations());
        taskEditText.setGravity(Gravity.LEFT);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectedAnimalFromListActivity.this)
                .setTitle("New observations")
                .setMessage("Please specify something concrete, easily identifiable or otherwise urgent regarding your animal.")
                .setView(taskEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newObs = taskEditText.getText().toString().trim().replace("\n", "");
                        editedAnimal.setObservations(newObs);
                        animalObsTV.setText(editedAnimal.getObservations());
                        Log.d(TAG, "onClick: editedAnimal obs = " + editedAnimal.getObservations());

                    }
                })
                .setNegativeButton("Cancel", null);

        return dialogBuilder;
    }

    private AlertDialog.Builder getEditSpeciesDialogBuilder() {
        final String originalValue = editedAnimal.getSpecies();
        int checkedItem = 0;
        if (editedAnimal.getSpecies().equals("dog")) checkedItem = 0;
        else if (editedAnimal.getSpecies().equals("cat")) checkedItem = 1;
        CharSequence[] array = {"Dog", "Cat"};

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectedAnimalFromListActivity.this)
                .setTitle("Change animal species")
                .setSingleChoiceItems(array, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Log.d(TAG, "onClick: Editing species = dog");
                            editedAnimal.setSpecies("dog");
                            animalIconIV.setImageResource(R.drawable.dog_icon);
                        } else {
                            Log.d(TAG, "onClick: Editing species = cat");
                            editedAnimal.setSpecies("cat");
                            animalIconIV.setImageResource(R.drawable.cat_icon);
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "CANCEL onClick: originalValue = " + originalValue);
                        if (originalValue.equals("cat")) {
                            Log.d(TAG, "onClick: reverting species to cat");
                            editedAnimal.setSpecies("cat");
                            animalIconIV.setImageResource(R.drawable.cat_icon);
                        } else if (originalValue.equals("dog")) {
                            Log.d(TAG, "onClick: reverting species to dog");
                            editedAnimal.setSpecies("dog");
                            animalIconIV.setImageResource(R.drawable.dog_icon);
                        }
                        dialog.dismiss();
                    }
                });

        return dialogBuilder;
    }


    private AlertDialog.Builder getAdoptableDialogBuilder() {
        String message;
        if (!editedAnimal.isAdoptable())
            message = "You are about to make " + animalNameTV.getText().toString() + " adoptable. Are you sure?";
        else
            message = "You are about to make " + animalNameTV.getText().toString() + " no longer adoptable. Are you sure?";

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectedAnimalFromListActivity.this);
        dialogBuilder.setTitle("Adoptable")
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editedAnimal.setAdoptable(!editedAnimal.isAdoptable());
                        if (editedAnimal.isAdoptable()) {
                            animalAdoptableImageView.setBackgroundResource(R.drawable.round_green_background);
                        } else {
                            animalAdoptableImageView.setBackgroundResource(R.drawable.round_red_background);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return dialogBuilder;
    }

    private AlertDialog.Builder getSaveChangesDialogBuilder() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectedAnimalFromListActivity.this);
        dialogBuilder.setTitle("Save changes?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: updated animal in database");
                        animalsDatabase.addAnimalToUser(editedAnimal, UserUtils.getCurrentUserId());
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: changes canceled, no updated were made");
                        dialog.cancel();
                        finish();
                    }
                });

        return dialogBuilder;
    }


    @Override
    public void onBackPressed() {

        Log.d(TAG, "onBackPressed: ENTERED HERE");
        Log.d(TAG, "onBackPressed: animal = " + animal.isAdoptable());
        Log.d(TAG, "onBackPressed: editedAnimal = " + editedAnimal.isAdoptable());

        if (!animal.equals(editedAnimal)) {
            AlertDialog dialog = getSaveChangesDialogBuilder().create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

//    private boolean validate() {
//        String animalName = newAnimalName.getText().toString().trim();
//        if (newAnimalAproxAge.getText().toString().isEmpty()) {
//            newAnimalAproxAge.setError("Please enter an age");
//            return false;
//        }
//        if (animalName.isEmpty()) {
//            newAnimalName.setError("Please write a name");
//            return false;
//        }
//        if(animalName.length() > 13) {
//            newAnimalName.setError("Name shouldn't exceed 13 letters");
//            return false;
//        }
//        if (newAnimalAproxAge.getText().toString().startsWith("0")) {
//            newAnimalAproxAge.setError("Age should not start with 0");
//            return false;
//        }
//        if (Integer.parseInt(newAnimalAproxAge.getText().toString()) > 2) {
//            newAnimalAdultCheckBox.setChecked(true);
//        }
//        return true;
//    }
}
