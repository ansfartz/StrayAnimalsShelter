package com.bcs.andy.strayanimalsshelter.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.database.AnimalPhotosDatabase;
import com.bcs.andy.strayanimalsshelter.database.AnimalPhotosDatabaseListener;
import com.bcs.andy.strayanimalsshelter.database.AnimalsDatabase;
import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.bcs.andy.strayanimalsshelter.utils.ImageUtils;
import com.bcs.andy.strayanimalsshelter.utils.UUIDGenerator;
import com.bcs.andy.strayanimalsshelter.utils.UserUtils;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

public class AddAnimalToMyselfActivity extends AppCompatActivity {

    private static final String TAG = "AddAnimalToMyselfActivity";
    private static final String CAMERA = Manifest.permission.CAMERA;
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int CAMERA_EXT_STORAGE_PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_IMAGE_CAPTURED_CODE = 201;

    // vars
    private File imageFile;
    private String pathToImageFile;
    private Boolean mCameraPermissionGranted = false;
    private Boolean hasPhotoAssigned = false;

    // Firebase
    private AnimalPhotosDatabase animalPhotosDatabase;
    private FirebaseUser firebaseUser;
    private AnimalsDatabase animalsDatabase;

    // UI
    private EditText newAnimalName;
    private EditText newAnimalAproxAge;
    private EditText newAnimalObservations;
    private Spinner newAnimalSpecies;
    private CheckBox newAnimalAdultCheckBox;
    private CheckBox newAnimalNeuteredCheckBox;
    private ImageView newAnimalPictureImageView;
    private FloatingActionButton acceptBtn;
    private FloatingActionButton cancelBtn;

    private void init() {
        newAnimalName = (EditText) findViewById(R.id.newAnimalNameET);
        newAnimalAproxAge = (EditText) findViewById(R.id.newAnimalAproxAgeET);
        newAnimalObservations = (EditText) findViewById(R.id.newAnimalObsET);
        newAnimalAdultCheckBox = (CheckBox) findViewById(R.id.newAnimalAdultCheckBox);
        newAnimalNeuteredCheckBox = (CheckBox) findViewById(R.id.newAnimalNeuteredCheckBox);

        newAnimalSpecies = (Spinner) findViewById(R.id.newAnimalTypeSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.types_of_animals, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newAnimalSpecies.setAdapter(spinnerAdapter);

        newAnimalPictureImageView = (ImageView) findViewById(R.id.newAnimalPictureImageView);
        newAnimalPictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        acceptBtn = (FloatingActionButton) findViewById(R.id.fab_acceptNewAnimal);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTlbLogic();
            }
        });
        cancelBtn = (FloatingActionButton) findViewById(R.id.fab_cancelNewAnimal);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void initFirebase() {
        animalsDatabase = new AnimalsDatabase();
        animalPhotosDatabase = new AnimalPhotosDatabase();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animal_to_myself);
        setTitle("Add Sheltered Animal");

//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//        getWindow().setLayout((int)(width * .8), (int)(height * .3));

        getCameraPermission();
        initFirebase();
        init();

    }


//    // for the Toolbar  that I created, to be used
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.add_animal_to_myself_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    // for the onClickListeners on toolbar
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.tlb1save:
//                saveTlbLogic();
//                return true;
//            case R.id.tlb1cancel:
//                finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void saveTlbLogic() {
        if (!validate()) {
            Toast.makeText(AddAnimalToMyselfActivity.this, "Please check field errors and try again", Toast.LENGTH_SHORT).show();
        } else {
            String userUid = UserUtils.getCurrentUserId();
            String animalID = UUIDGenerator.createUUID();
            String name = newAnimalName.getText().toString().trim();
            String species = newAnimalSpecies.getSelectedItem().toString();
            String observations = newAnimalObservations.getText().toString().trim();
            Integer aproxAge = Integer.parseInt(newAnimalAproxAge.getText().toString().trim());
            Boolean isAdult = newAnimalAdultCheckBox.isChecked();
            Boolean isNeutered = newAnimalNeuteredCheckBox.isChecked();

            Animal animal = new Animal(userUid, animalID, name, species, isAdult, isNeutered, aproxAge, observations, false);


            if(hasPhotoAssigned) {

                animalPhotosDatabase.addPhotoToAnimalGetURL(animal, pathToImageFile, new AnimalPhotosDatabaseListener() {
                    @Override
                    public void onPhotoUploadComplete(String uriString) {
                        Log.d(TAG, "onPhotoUploadComplete: URL = " + uriString);
                        animal.setPhotoLink(uriString);
                        animalsDatabase.addAnimalToUser(animal, userUid);
                    }
                });

            } else {

                animalsDatabase.addAnimalToUser(animal, userUid);

            }





            finish();
        }
    }

    public boolean validate() {
        String animalName = newAnimalName.getText().toString().trim();
        if (newAnimalAproxAge.getText().toString().isEmpty()) {
            newAnimalAproxAge.setError("Please enter an age");
            return false;
        }
        if (animalName.isEmpty()) {
            newAnimalName.setError("Please write a name");
            return false;
        }
        if(animalName.length() > 13) {
            newAnimalName.setError("Name shouldn't exceed 13 letters");
            return false;
        }
        if (newAnimalAproxAge.getText().toString().startsWith("0")) {
            newAnimalAproxAge.setError("Age should not start with 0");
            return false;
        }
        if (Integer.parseInt(newAnimalAproxAge.getText().toString()) > 2) {
            newAnimalAdultCheckBox.setChecked(true);
        }
        return true;
    }

    private void getCameraPermission() {
        Log.d(TAG, "getCameraPermission: GETTING CAMERA / EXTERNAL STORAGE PERMISSIONS");
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            mCameraPermissionGranted = true;
        else
            ActivityCompat.requestPermissions(this, permissions, CAMERA_EXT_STORAGE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: CHECKING PERMISSION RESULT");
        mCameraPermissionGranted = false;

        switch (requestCode) {
            case CAMERA_EXT_STORAGE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mCameraPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: PERMISSION DENIED");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: PERMISSION GRANTED");
                    mCameraPermissionGranted = true;
                }
            }
        }
    }

    private void takePicture() {
        if (mCameraPermissionGranted) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                imageFile = null;

                try {
                    imageFile = ImageUtils.createPhotoFile();
                    if (imageFile != null) {
                        pathToImageFile = imageFile.getAbsolutePath();
                        Uri imageURI = FileProvider.getUriForFile(AddAnimalToMyselfActivity.this, "com.bcs.andy.strayanimalsshelter.ui.AddAnimalToMyselfActivity.fileprovider", imageFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURED_CODE);

                    }
                } catch (Exception e) {
                    Log.d(TAG, "takePicture: Exception caught");
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "takePicture: takePictureIntent.resolveActivity(getPackageManager()) == null");
            }
        }
        else {
            Toast.makeText(this, "You must allow Camera permission in order to take animal pictures", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURED_CODE) {
            hasPhotoAssigned = true;
            Bitmap bitmap = BitmapFactory.decodeFile(pathToImageFile);
            bitmap = ImageUtils.rotateBitmapIfNecessary(bitmap, pathToImageFile);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(pathToImageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            newAnimalPictureImageView.setImageBitmap(bitmap);
        }
    }


}
