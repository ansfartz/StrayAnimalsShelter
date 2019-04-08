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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.database.AnimalPhotosDatabase;
import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.bcs.andy.strayanimalsshelter.utils.ImageUtils;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

public class AddAnimalForMarkerActivity extends AppCompatActivity {


    private static final String TAG = "AddAnimalForMarkerActiv";
    private static final String CAMERA = Manifest.permission.CAMERA;
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int CAMERA_EXT_STORAGE_PERMISSION_REQUEST_CODE = 200;
    private static final int ANIMAL_FOR_MARKER_REQUEST_CODE_SUCCESS = 222;
    private static final int ANIMAL_FOR_MARKER_REQUEST_CODE_FAIL = 422;
    private static final int REQUEST_IMAGE_CAPTURED_CODE = 201;


    // Firebase
    private AnimalPhotosDatabase animalPhotosDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference markersRef;


    // vars
    private File imageFile;
    private String pathToImageFile;
    private Boolean mCameraPermissionGranted = false;
    private Boolean hasPhotoAssigned = false;

    // UI
    private Toolbar toolbar;
    private EditText animalName;
    private EditText animalAproxAge;
    private EditText animalObservations;
    private Spinner animalSpecies;
    private CheckBox animalAdultCheckBox;
    private CheckBox animalNeuteredCheckBox;
    private ImageView animalPictureImageView;
    private FloatingActionButton acceptBtn;
    private FloatingActionButton cancelBtn;


    private void init() {

        animalName = (EditText) findViewById(R.id.newAnimalForMarkerNameET);
        animalAproxAge = (EditText) findViewById(R.id.newAnimalForMarkerAproxAgeET);
        animalObservations = (EditText) findViewById(R.id.newAnimalForMarkerObsET);
        animalAdultCheckBox = (CheckBox) findViewById(R.id.newAnimalForMarkerAdultCheckBox);
        animalNeuteredCheckBox = (CheckBox) findViewById(R.id.newAnimalForMarkerNeuteredCheckBox);

        animalSpecies = (Spinner) findViewById(R.id.newAnimalForMarkerTypeSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.types_of_animals, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        animalSpecies.setAdapter(spinnerAdapter);

        animalPictureImageView = (ImageView) findViewById(R.id.newAnimalForMarkerPictureImageView);
        animalPictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        cancelBtn = (FloatingActionButton) findViewById(R.id.fab_cancelNewAnimalForMarker);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });

        acceptBtn = (FloatingActionButton) findViewById(R.id.fab_acceptNewAnimalForMarker);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validate()) {
                    Toast.makeText(AddAnimalForMarkerActivity.this, "Please check field errors and try again", Toast.LENGTH_SHORT).show();
                } else {
                    String animalID = UUID.randomUUID().toString();
                    String name = animalName.getText().toString().trim();
                    String species = animalSpecies.getSelectedItem().toString();
                    String obs = animalObservations.getText().toString();
                    Integer aproxAge = Integer.parseInt(animalAproxAge.getText().toString().trim());
                    boolean isAdult = animalAdultCheckBox.isChecked();
                    boolean isNeutered = animalNeuteredCheckBox.isChecked();

                    Animal animal = new Animal(animalID, name, species, isAdult, isNeutered, aproxAge, obs);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("animal", animal);

                    if(hasPhotoAssigned){
                        resultIntent.putExtra("photoPath", pathToImageFile);
                    }
                    setResult(RESULT_OK, resultIntent);
                    finish();

                }

            }
        });


    }

    private void initFirebase() {
        animalPhotosDatabase = new AnimalPhotosDatabase();
        markersRef = FirebaseDatabase.getInstance().getReference("markers");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animal_for_marker);
        setTitle("Add Animal for Marker");

        getCameraPermission();
        initFirebase();
        init();
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
                        Uri imageURI = FileProvider.getUriForFile(AddAnimalForMarkerActivity.this, "com.bcs.andy.strayanimalsshelter.ui.AddAnimalToMyselfActivity.fileprovider", imageFile);
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

            // save the rotated bitmap to the same/old path
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            animalPictureImageView.setImageBitmap(bitmap);
        }
    }

    public boolean validate() {
        String animalNameString = animalName.getText().toString().trim();
        if (animalAproxAge.getText().toString().isEmpty()) {
            animalAproxAge.setError("Please enter an age");
            return false;
        }
        if (animalNameString.isEmpty()) {
            animalName.setError("Please write a name");
            return false;
        }
        if(animalNameString.length() > 13) {
            animalName.setError("Name shouldn't exceed 13 letters");
            return false;
        }

        if (animalAproxAge.getText().toString().startsWith("0")) {
            animalAproxAge.setError("Age can't be 0");
            return false;
        }
        if (Integer.parseInt(animalAproxAge.getText().toString()) > 2) {
            animalAdultCheckBox.setChecked(true);
        }
        return true;
    }

}
