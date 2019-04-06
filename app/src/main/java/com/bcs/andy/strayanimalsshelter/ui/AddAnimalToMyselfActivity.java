package com.bcs.andy.strayanimalsshelter.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
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
import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AddAnimalToMyselfActivity extends AppCompatActivity {

    private static final String TAG = "AddAnimalToMyselfActivity";
    private static final String CAMERA = Manifest.permission.CAMERA;
    private static final String EXTSTORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int CAMERA_EXTSTORAGE_PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_IMAGE_CAPTURED_CODE = 201;

    private File imageFile;
    private String pathToImageFile;

    // vars
    private Boolean mCameraPermissionGranted = false;

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
    private ImageView newAnimalPictureImageView;

    private void init() {
        newAnimalName = (EditText) findViewById(R.id.newAnimalNameET);
        newAnimalAproxAge = (EditText) findViewById(R.id.newAnimalAproxAgeET);
        newAnimalObservations = (EditText) findViewById(R.id.newAnimalObsET);
        newAnimalAdultCheckBox = (CheckBox) findViewById(R.id.newAnimalAdultCheckBox);
        newAnimalNeuteredCheckBox = (CheckBox) findViewById(R.id.newAnimalNeuteredCheckBox);

        newAnimalPictureImageView = (ImageView) findViewById(R.id.newAnimalPictureImageView);
        newAnimalPictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        newAnimalSpecies = (Spinner) findViewById(R.id.newAnimalTypeSpinner);
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
        setContentView(R.layout.activity_add_animal_to_myself);
        setTitle("Add Sheltered Animal");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//        getWindow().setLayout((int)(width * .8), (int)(height * .3));

        getCameraPermission();
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
            Toast.makeText(AddAnimalToMyselfActivity.this, "Please check field errors and try again", Toast.LENGTH_SHORT).show();
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
        if (newAnimalAproxAge.getText().toString().startsWith("0")) {
            newAnimalAproxAge.setError("Age should not start with 0");
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
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), EXTSTORAGE) == PackageManager.PERMISSION_GRANTED)
            mCameraPermissionGranted = true;
        else
            ActivityCompat.requestPermissions(this, permissions, CAMERA_EXTSTORAGE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: CHECKING PERMISSION RESULT");
        mCameraPermissionGranted = false;

        switch (requestCode) {
            case CAMERA_EXTSTORAGE_PERMISSION_REQUEST_CODE: {
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
                    imageFile = createPhotoFile();
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

    }



    private File createPhotoFile() {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File tempFile = null;
        try {
            tempFile = File.createTempFile("StrayAnimalsShelter-", ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURED_CODE) {
            Bitmap bitmap = BitmapFactory.decodeFile(pathToImageFile);
            bitmap = rotateBitmapIfNecessary(bitmap, pathToImageFile);
            newAnimalPictureImageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap rotateBitmapIfNecessary(Bitmap myBitmap, String path) {
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myBitmap;
    }


}
